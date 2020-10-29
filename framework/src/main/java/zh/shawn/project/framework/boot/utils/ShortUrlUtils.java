package zh.shawn.project.framework.boot.utils;

/**

 * @description: 短地址
 * 长度设为6位，每一位由[a-z,A-Z,0-9]总共62个字母组成
 * id为integer类型，数据库中需要设置为自增主键
 * 表结构
 * ----------------------------------
 * ID    LURL    SURL
 * ----------------------------------
 * ID 自增主键
 * LURL 唯一约束
 * @author: zhangxiulin
 * @time: 2020/7/21 17:48
 */
public class ShortUrlUtils {

    //public static final String alphabetStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};

    public static final int baseLen = alphabet.length;


    /**
     *@Description: base62编码的短址解码得到短地址映射表的主键ID
     *@params:
     *@return:
     */
    public static int decode(String base62){
        //char[] chars = base62.toCharArray();
        char[] chars = getOriginShortUrl(base62).toCharArray();
        int id = 0;
        int pow = chars.length-1;
        for(int i = 0; i < chars.length; i++){
            id +=  getIndexOfCharArr(alphabet, chars[i]) * Math.pow(baseLen, pow);
            pow--;
        }
        return id;
    }

    /**
     *@Description: 根据短地址映射表的主键ID生成短址
     *@params:
     *@return:
     */
    public static String encode(int id){
        int[] arr = base62Arr(id);
        //return base62(arr);
        return getConversionUrl(base62(arr));
    }

    public static String base62(int[] base62Arr){
        // 确保base62数组长度是6，不足6位前面补0
        int[] _base62Arr = new int[6];
        if(base62Arr.length == 6){
            _base62Arr = base62Arr;
        }else{
            for(int i = 6-base62Arr.length, j = 0; i > 0 && j < base62Arr.length ; i--, j++){
                _base62Arr[i] = base62Arr[j];
            }
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < _base62Arr.length; i++){
            sb.append(alphabet[_base62Arr[i]]);
        }
        return sb.toString();
    }

    public static int[] base62Arr(int id){
        int[] base62 = {0,0,0,0,0,0};
        int index = 5;
        while(id > 0){
            int rem = (int)id % baseLen;
            base62[index] = rem;
            id = id / baseLen;
            index--;
        }
        return base62;
    }

    public static int getIndexOfCharArr(char[] chars, char el){
        int index = -1;
        for(int i = 0; i < chars.length; i++){
            if(el == chars[i]){
                index = i;
                break;
            }
        }
        return index;
    }

    //  base62 容易被反解出数据库主键ID，所以在返回给客户端的时候先将短址进行部分字符换位
    //  “换位的起始位置”通过短址的每一位对应的字符表数组下标求和，然后除以6 取余得到
    /**
     *@Description: 将短址部分字符换位
     *@params: [shortUrl 数据库中存储的真正的短址]
     *@return: java.lang.String
     */
    public static String getConversionUrl(String shortUrl){
        char[] chars = shortUrl.toCharArray();
        int id = 0;
        for(int i = 0; i < chars.length; i++){
            id +=  getIndexOfCharArr(alphabet, chars[i]);
        }
        int yu = id % 6;
        String newUrl = StringUtil.reversePart(shortUrl, yu);
        return newUrl;
    }

    /**
     *@Description: 将换位后的短址反向换位得到数据库中存储的短址
     *@params: [url]
     *@return: java.lang.String
     */
    public static String getOriginShortUrl(String url){
        char[] chars = url.toCharArray();
        int id = 0;
        for(int i = 0; i < chars.length; i++){
            id +=  getIndexOfCharArr(alphabet, chars[i]);
        }
        int c = 6 - (id % 6);
        String originUrl = StringUtil.reversePart(url, c);
        return originUrl;
    }

    public static void main(String args[]){
        System.out.println("0:" + encode(0) + ":" + decode(encode(0)));
        System.out.println(encode(1));
        System.out.println(encode(138));
        System.out.println(138 + ":" + encode(138) + ":" + decode(encode(138)));
        System.out.println(encode(1380000000));
        System.out.println(encode(1380000001));
        System.out.println(encode(1380000002));
        System.out.println("1380000002:" + encode(1380000002) + ":" + decode(encode(1380000002)));
        System.out.println(encode(Integer.MIN_VALUE));
        System.out.println(encode(Integer.MAX_VALUE));
        System.out.println(Integer.MAX_VALUE + ":" + encode(Integer.MAX_VALUE) + ":" + decode(encode(Integer.MAX_VALUE)));
        System.out.println("-----------------------");

        String shortUrl = "aaaabc";
        System.out.println("原短址："  + shortUrl);
        System.out.println("换位后短址："  + getConversionUrl(shortUrl));
        System.out.println("还原后短址："  + getOriginShortUrl(getConversionUrl(shortUrl)));
    }
}
