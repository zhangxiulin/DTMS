package zh.shawn.project.pure.boot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang.StringUtils;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 转发socket请求
 */
public class SocketUtils {
    private static Logger log = LoggerFactory.getLogger(SocketUtils.class);

    private Connection conn;

    public SocketUtils() {

    }

    public SocketUtils(Connection conn) {
        this.conn = conn;
    }


    /**
     * socket 发送xml格式
     *
     * @param flowCode
     * @param ip
     * @param port
     * @param root
     * @param begin
     * @param end
     * @param map
     * @param replaceKey
     * @param filterKey
     * @return
     * @throws Exception
     */
    public Map<String, Object> socketXmlInfo(String flowCode, String ip, int port, Object root, Object begin, Object end,
                                             Map<String, Object> map, Object replaceKey, Object filterKey) throws Exception {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xm = new XmlMapper(module);
        // set xml mapper
        log.debug("即将处理数据");
        String xml = "";
        try {
            if (root != null && root.toString().length() > 1) {
                log.debug("修改root字段");
                String middle = xm.writer().withRootName(root.toString()).writeValueAsString(map.get(root));
                log.debug("格式包装:" + middle);
                xml = (begin != null && begin.toString().length() > 1 ? begin.toString() : "") + middle;
                xml = xml + (end != null && end.toString().length() > 1 ? end.toString() : "");
            } else {
                log.debug("不修改root字段");
                String middle = xm.writer().withoutRootName().writeValueAsString(map);
                middle = middle.replace("<>", "");
                middle = middle.replace("</>", "");
                log.debug("格式包装:" + middle);
                xml = (begin != null && begin.toString().length() > 1 ? begin.toString() : "") + middle;
                xml = xml + (end != null && end.toString().length() > 1 ? end.toString() : "");
            }

            //发送消息
            return socketClientMsg(ip, port, xml, replaceKey, filterKey, flowCode);

        } catch (RuntimeException e) {
            log.error("数据转换失败inXML1", e);
            throw new Exception("数据转换失败", e);
        } catch (Exception e) {
            log.error("数据转换失败inXML2", e);
            throw new Exception("数据转换失败", e);
        }

    }

    /**
     * 处理socket 发送 xml报文消息
     *
     * @param ip
     * @param port
     * @param xmlStr
     * @param replaceKey
     * @param filterKey
     * @param flowCode
     * @return
     * @throws Exception
     */
    private Map<String, Object> socketClientMsg(String ip, int port, String xmlStr, Object replaceKey, Object filterKey, String flowCode) throws Exception {

        //消息内容长度
        String headLengthStr = "" + xmlStr.getBytes("UTF-8").length;
        String xmlHead = "";
        if (headLengthStr.length() < 8) {
            for (int i = 0; i < (8 - headLengthStr.length()); i++) {
                xmlHead += "0";
            }
            xmlHead += headLengthStr;
        } else {
            xmlHead = headLengthStr;
        }

        StringBuilder sendXml = new StringBuilder();
        //贷款基本信息
        sendXml.append(xmlHead.toString() + xmlStr);

        String sendXmlStr = sendXml.toString();

        log.info("发送 socket报文，地址：[" + ip + ":" + port + "]，内容:[" + sendXmlStr + "]");

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;

        String returnStr = "";
        try {

            byte[] xmlStrBytes = sendXmlStr.getBytes("UTF-8");

            //构建Socket连接
            socket = new Socket(ip, port);
            out = socket.getOutputStream();
            out.write(xmlStrBytes);//发送消息

            //接收返回内容
            in = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            String retmsg = "";
            StringBuffer ret = new StringBuffer();
            while (!((retmsg = br.readLine()) == null)) {
                ret.append(URLDecoder.decode(retmsg.toString(), "UTF-8"));
            }

            retmsg = ret.toString();

            returnStr =retmsg.trim();//返回结果
        } catch (Exception e) {
            log.error("处理socket请求异常!", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }


        // 设置基本请求
        Map<String, Object> dMap = new HashMap<>();
        String returnMsg = "";
        ObjectMapper om = new ObjectMapper();
        try {
            if (StringUtils.isNotBlank(returnStr)) {
                returnMsg = returnStr;
                log.info("返回报文原文：" + returnMsg);
                // new response deal
                Pattern pattern = Pattern.compile(HttpClientUtil.XML_REGEX);
                for (String rk : replaceKey != null && replaceKey.toString().contains(HttpClientUtil.XML_SEPERATE)
                        ? replaceKey.toString().split("[" + HttpClientUtil.XML_SEPERATE + "]")
                        : new String[]{replaceKey.toString()}) {
                    returnMsg = returnMsg.replaceAll(rk, "");
                    log.debug("response替换报文[" + rk + "]：" + returnMsg);
                }
                log.debug("开始过滤报文关键过滤字段：" + returnMsg);
                Matcher matcher = pattern.matcher(returnMsg);
                List<String> mss = new ArrayList<>();
                log.debug("查询xml所有标签");
                while (matcher.find()) {
                    mss.add(matcher.group());
                    log.debug("xml标签：" + matcher.group());
                }

                Object[] os = mss.stream().filter(s -> {
                    for (String fk : filterKey != null && filterKey.toString().contains(HttpClientUtil.XML_SEPERATE)
                            ? filterKey.toString().split("[" + HttpClientUtil.XML_SEPERATE + "]")
                            : new String[]{filterKey.toString()}) {
                        if (s.contains(fk)) {
                            return true;
                        }
                    }
                    return false;
                }).toArray();
                List<String> filteredItems = new LinkedList<>();
                for (Object o : os) {
                    if (o.toString().contains(" ")) {
                        String tos = "</" + o.toString().replaceAll("[<>]", "").split("[ ]")[0] + ">";
                        filteredItems.add(tos);
                        filteredItems.add(o.toString());
                        // System.out.println(o + "," + tos);
                        log.debug("已匹配过滤标签为：" + o + "," + tos);
                    }
                    // System.out.println(o.toString());
                }
                for (String s : filteredItems) {
                    returnMsg = returnMsg.replace(s, "");
                    log.debug("替换过滤标签[" + s + "]：" + returnMsg);
                }
                om.enable(SerializationFeature.INDENT_OUTPUT);
                // System.out.println(om.readValue(jo.toString(), Object.class));
                Object omap = om.readValue(XML.toJSONObject(returnMsg, true).toString(), Object.class);
                if (omap instanceof Map) {
                    dMap = (Map) omap;
                } else {
                    dMap.put("rdata", omap);
                }
                // new response deal end
                log.info("返回报文：" + dMap);

            } else {

                log.info("发送 socket报文，地址：[" + ip + ":" + port + "]，内容:[" + sendXmlStr + "]，无返回数据!");

            }

            return dMap;
        } catch (Exception e) {
            log.error("返回报文处理失败", e);
            throw new Exception("业务服务处理失败，返回报文为非预定格式报文。");
        } finally {
            dMap = null;
        }

    }
}
