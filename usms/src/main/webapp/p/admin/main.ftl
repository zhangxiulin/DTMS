<!DOCTYPE html>

<html lang="zh-cmn-Hans" app="main">
<!-- begin::Head -->

<head>
    <meta charset="utf-8" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Cache-Control" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>分布式事务管理平台</title>
    <meta name="description" content="分布式事务管理平台">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no">
    <style>
    </style>
    <!--begin::Web font -->
    <script src="${fep}/assets/webfont.js"></script>
    <script>
        WebFont.load({
            google: { "families": ["Poppins:300,400,500,600,700", "Roboto:300,400,500,600,700"] },
            active: function () {
                sessionStorage.fonts = true;
            }
        });
    </script>
    <!--end::Web font -->
    <!--begin:: Global Mandatory Vendors -->
    <link href="${fep}/vendors/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css" />
    <!--end:: Global Mandatory Vendors -->
    <!--begin:: Global Optional Vendors -->
    <link href="${fep}/vendors/tether/dist/css/tether.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-datepicker/dist/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-datetime-picker/css/bootstrap-datetimepicker.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-timepicker/css/bootstrap-timepicker.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-daterangepicker/daterangepicker.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-switch/dist/css/bootstrap3/bootstrap-switch.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-select/dist/css/bootstrap-select.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/select2/dist/css/select2.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/nouislider/distribute/nouislider.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/owl.carousel/dist/./assets/owl.carousel.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/owl.carousel/dist/./assets/owl.theme.default.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/ion-rangeslider/css/ion.rangeSlider.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/ion-rangeslider/css/ion.rangeSlider.skinFlat.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/dropzone/dist/dropzone.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/summernote/dist/summernote.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-markdown/css/bootstrap-markdown.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/animate.css/animate.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/toastr/build/toastr.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/jstree/dist/themes/default/style.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/morris.js/morris.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/chartist/dist/chartist.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/sweetalert2/dist/sweetalert2.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/socicon/css/socicon.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/./vendors/line-awesome/css/line-awesome.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/./vendors/flaticon/css/flaticon.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/./vendors/metronic/css/styles.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/./vendors/fontawesome5/css/all.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/picture/css/pictureViewer.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}/vendors/bootstrap-fileinput/css/fileinput.min.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <link href="${fep}assets/vendors/custom/datatables/datatables.bundle.css?version=1.0.0.0.1" rel="stylesheet" type="text/css" />
    <!--begin::Global Theme Styles -->
    <script> document.write('<link href="${fep}/assets/demo/base/style.bundle.css?v=' + new Date().getTime() + ' " rel="stylesheet" type="text/css" ><\/link>')</script>
    <script> document.write('<link href="${fep}/static/css/common.css?v=' + new Date().getTime() + ' " rel="stylesheet" type="text/css" ><\/link>')</script>
    <!--end::Global Theme Styles -->
    <link rel="shortcut icon" href="${fep}assets/app/media/img/logos/favicon.png?version=1.0.0.0.1" />
</head>
<body id="body" class="m-page--fluid m--skin- m-content--skin-light2 m-page--loading-enabled m-page--loading m-header--fixed m-header--fixed-mobile m-aside-left--enabled m-aside-left--skin-dark m-aside-left--offcanvas m-footer--push m-aside--offcanvas-default">

<div class="m-page-loader m-page-loader--base">
    <div class="m-blockui">
        <span>资源加载中请稍后......</span>
        <span>
                <div class="m-loader m-loader--brand"></div>
            </span>
        <div id="loadpro_all" class="progress m-progress--sm">
            <div class="progress-bar m--bg-accent" role="progressbar" style="width: 50%;" aria-valuenow="50"
                 aria-valuemin="0" aria-valuemax="100"></div>
        </div>
    </div>
</div>

<!-- begin:: Page -->
<div class="m-grid m-grid--hor m-grid--root m-page">

    <!-- BEGIN: Header -->
    <header id="m_header" class="m-grid__item m-header " m-minimize-offset="200" m-minimize-mobile-offset="200">
        <div class="m-container m-container--fluid m-container--full-height">
            <div class="m-stack m-stack--ver m-stack--desktop">

                <!-- BEGIN: Brand -->
                <div class="m-stack__item m-brand  m-brand--skin-dark ">
                    <div class="m-stack m-stack--ver m-stack--general">
                        <div class="m-stack__item m-stack__item--middle m-brand__logo">
                            <a class="m-brand__logo-wrapper">
                                <img alt="" src="${fep}/assets/app/main/img/logo.png" />
                            </a>
                        </div>
                        <div class="m-stack__item m-stack__item--middle m-brand__tools">

                            <!-- BEGIN: Left Aside Minimize Toggle -->
                            <a id="m_aside_left_minimize_toggle" class="m-brand__icon m-brand__toggler m-brand__toggler--left m--visible-desktop-inline-block  ">
                                <span></span>
                            </a>
                            <!-- END -->
                            <!-- BEGIN: Responsive Aside Left Menu Toggler -->
                            <a id="m_aside_left_offcanvas_toggle" class="m-brand__icon m-brand__toggler m-brand__toggler--left m--visible-tablet-and-mobile-inline-block">
                                <span></span>
                            </a>
                            <!-- END -->
                            <!-- BEGIN: Responsive Header Menu Toggler -->
                            <a id="m_aside_header_menu_mobile_toggle" class="m-brand__icon m-brand__toggler m--visible-tablet-and-mobile-inline-block">
                                <span></span>
                            </a>
                            <!-- END -->
                            <!-- BEGIN: Topbar Toggler -->
                            <a id="m_aside_header_topbar_mobile_toggle" class="m-brand__icon m--visible-tablet-and-mobile-inline-block">
                                <i class="flaticon-more"></i>
                            </a>
                            <!-- BEGIN: Topbar Toggler -->
                        </div>
                    </div>
                </div>
                <!-- END: Brand -->
                <div class="m-stack__item m-stack__item--fluid m-header-head" id="m_header_nav">

                    <!-- BEGIN: Horizontal Menu -->
                    <button class="m-aside-header-menu-mobile-close  m-aside-header-menu-mobile-close--skin-dark "
                            id="m_aside_header_menu_mobile_close_btn"><i class="la la-close"></i></button>
                    <div id="m_header_menu" class="m-header-menu m-aside-header-menu-mobile m-aside-header-menu-mobile--offcanvas  m-header-menu--skin-light m-header-menu--submenu-skin-light m-aside-header-menu-mobile--skin-dark m-aside-header-menu-mobile--submenu-skin-dark ">
                        <ul id="app_menu" class="m-menu__nav  m-menu__nav--submenu-arrow ">

                        </ul>
                    </div>
                    <!-- END: Horizontal Menu -->
                    <!-- BEGIN: Topbar -->
                    <div id="m_header_topbar" class="m-topbar  m-stack m-stack--ver m-stack--general">
                        <div class='la la-star' id="collect" onclick="openCollection()" style='position:absolute;top:1.2rem;right:25rem;color:#fcfcfc;font-size:3.1rem;z-index:1;cursor: pointer;'><span
                                style='border-radius:50% 50%;text-align:center;font-size:0.45rem;display:block;float:right;padding:3px;margin-top: -9px;margin-left: -19px;background-color:#f4516c;color:#eee'
                                id="collection_num">0</span></div>
                        <div class="m-stack__item m-topbar__nav-wrapper">
                            <ul class="m-topbar__nav m-nav m-nav--inline">

                                <li class="m-nav__item" id="network_status">
                                    <a id="network_status_msg" class="m-nav__link" data-container="body"
                                       data-toggle="m-popover" data-placement="bottom" data-content="">
                                        <span id="network_status_icon" class="m-nav__link-badge m-badge m-badge--dot m-badge--warning"></span>
                                        <span class="m-nav__link-icon">
                                                <span class="m-nav__link-icon-wrapper"><i class="socicon-rss"></i></span>
                                            </span>
                                    </a>
                                </li>

                                <li class="m-nav__item m-topbar__notifications m-dropdown m-dropdown--large m-dropdown--arrow m-dropdown--align-center 	m-dropdown--mobile-full-width"
                                    m-dropdown-toggle="click" m-dropdown-persistent="1">
                                    <a class="m-nav__link m-dropdown__toggle" id="m_topbar_event_icon">
                                            <span class="m-nav__link-icon">
                                                <span class="m-nav__link-icon-wrapper"><i class="flaticon-alarm"></i></span>
                                                <span class="m-nav__link-badge m-badge m-badge--danger" id="topbar_event_num"></span>
                                            </span>
                                    </a>
                                    <div class="m-dropdown__wrapper" id="dropdown__wrapper">
                                        <span class="m-dropdown__arrow m-dropdown__arrow--center"></span>
                                        <div class="m-dropdown__inner">
                                            <div class="m-dropdown__header m--align-center">
                                                <span class="m-dropdown__header-title">重要提醒</span>
                                            </div>
                                            <div class="m-dropdown__body">
                                                <div class="m-dropdown__content">
                                                    <ul class="nav nav-tabs m-tabs m-tabs-line m-tabs-line--brand"
                                                        role="tablist">
                                                        <li class="nav-item m-tabs__item" style="border-bottom:none">
                                                            <a class="nav-link m-tabs__link active" data-toggle="tab"
                                                               href="#topbar_event_publics" role="tab">
                                                                消息公告
                                                            </a>
                                                        </li>
                                                        <li class="nav-item m-tabs__item" style="border-bottom:none">
                                                            <a class="nav-link m-tabs__link" data-toggle="tab" href="#topbar_event_notifications"
                                                               role="tab">培训及会议通知</a>
                                                        </li>

                                                    </ul>
                                                    <div class="tab-content">
                                                        <div class="tab-pane active" id="topbar_event_publics" role="tabpanel">
                                                            <div class="m-scrollable" data-scrollable="true"
                                                                 data-height="250" data-mobile-height="200">
                                                                <div class="m-list-timeline m-list-timeline--skin-light">
                                                                    <div class="m-list-timeline__items">
                                                                        <div class="m-list-timeline__item">
                                                                            <span class="m-list-timeline__badge -m-list-timeline__badge--state-success"></span>
                                                                            <span class="m-list-timeline__text">系统日志1</span>
                                                                            <span class="m-list-timeline__time">Just
                                                                                    now</span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="tab-pane" id="topbar_event_notifications" role="tabpanel">
                                                            <div class="m-scrollable" data-scrollable="true"
                                                                 data-height="250" data-mobile-height="200">
                                                                <div class="m-list-timeline">
                                                                    <div class="m-list-timeline__items">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="tab-pane" id="topbar_event_reminder" role="tabpanel">
                                                            <div class="m-scrollable" data-scrollable="true"
                                                                 data-height="250" data-mobile-height="200">
                                                                <div class="m-list-timeline m-list-timeline--skin-light">
                                                                    <div class="m-list-timeline__items">
                                                                        <div class="m-list-timeline__item">
                                                                            <span class="m-list-timeline__badge -m-list-timeline__badge--state-success"></span>
                                                                            <span class="m-list-timeline__text">操作日志1</span>
                                                                            <span class="m-list-timeline__time">Just
                                                                                    now</span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="m-nav__item m-topbar__user-profile  m-dropdown m-dropdown--medium m-dropdown--arrow m-dropdown--align-right m-dropdown--mobile-full-width m-dropdown--skin-light"
                                    m-dropdown-toggle="click">
                                    <a class="m-nav__link m-dropdown__toggle">
                                        <span class="m-topbar__username m--hidden-mobile">您好，</span><span class="m-topbar__username m--hidden-mobile welc_user_name"></span>
                                        <span class="m-topbar__userpic">
                                                <img src="${fep}/assets/app/media/img/head.png" />
                                            </span>
                                        <span class="m-nav__link-icon m-topbar__usericon  m--hide">
                                                <span class="m-nav__link-icon-wrapper"><i class="flaticon-user-ok"></i></span>
                                            </span>
                                    </a>
                                    <div class="m-dropdown__wrapper">
                                        <span class="m-dropdown__arrow m-dropdown__arrow--right m-dropdown__arrow--adjust"></span>
                                        <div class="m-dropdown__inner">
                                            <div class="m-dropdown__header m--align-center">
                                                <div class="m-card-user m-card-user--skin-light">
                                                    <div class="m-card-user__pic">
                                                        <img src="${fep}/assets/app/media/img/head.png" class="m--img-rounded m--marginless"
                                                             alt="" />
                                                    </div>
                                                    <div class="m-card-user__details">
                                                        <span class="m-card-user__name m--font-weight-500 welc_user_name"></span>
                                                        <a href="#" class="m-card-user__email m--font-weight-300 m-link welc_user_mail"></a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="m-dropdown__body">
                                                <div class="m-dropdown__content">
                                                    <ul class="m-nav m-nav--skin-light">
                                                        <li class="m-nav__section m--hide">
                                                            <span class="m-nav__section-text">Section</span>
                                                        </li>

                                                        <li class="m-nav__item">
                                                            <a href="javascript:exitApp();" class="btn m-btn--pill    btn-secondary m-btn m-btn--custom m-btn--label-brand m-btn--bolder">安全退出</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <!-- END: Topbar -->
                </div>
            </div>
        </div>
    </header>

    <!-- END: Header -->

    <!-- begin::Body -->
    <div class="m-grid__item m-grid__item--fluid m-grid m-grid--ver-desktop m-grid--desktop m-body">

        <!-- BEGIN: Left Aside -->
        <button class="m-aside-left-close  m-aside-left-close--skin-dark " id="m_aside_left_close_btn"><i class="la la-close"></i></button>
        <div id="m_aside_left" class="m-grid__item	m-aside-left  m-aside-left--skin-dark ">
            <!-- BEGIN: Aside Menu -->
            <div id="m_ver_menu" class="m-aside-menu  m-aside-menu--skin-dark m-aside-menu--submenu-skin-dark "
                 m-menu-vertical="1" m-menu-scrollable="0" m-menu-dropdown-timeout="500">
                <div id="menuarea" class="col-md-14">
                    <i class="la la-search" style="font-size:1.1rem;position: relative;color:#eee;left:10rem"></i>
                    <input id="searchmenu" class="form-control m-input" placeholder="搜索" autocomplete="off">
                    <!-- <span class="m-nav__link-icon" id="querymenu"><i class="flaticon-search-1" style="font-size:1.1rem"></i></span> -->
                </div>
                <ul class="m-menu__nav" id="main_menu">
                </ul>
            </div>

            <!-- END: Aside Menu -->
        </div>
        <!-- END: Left Aside -->
        <div id="main_content" class="m-grid__item m-grid__item--fluid m-wrapper">
            <!-- BEGIN: Subheader -->
            <div class="m-subheader ">
                <div class="d-flex align-items-center">
                    <div class="mr-auto">
                        <h3 class="m-subheader__title m-subheader__title--separator">锦E行综合管理门户</h3>
                        <ul class="m-subheader__breadcrumbs m-nav m-nav--inline" id="breadcump">


                        </ul>
                    </div>

                </div>
            </div>
            <!-- BEGIN: Subheader -->
            <div id="page_content">
            </div>
        </div>
    </div>
    <!-- end:: Body -->

    <!-- begin::Footer -->
    <footer class="m-grid__item		m-footer " style='position: fixed;bottom: 0.1rem;z-index: 3;padding: 1px 30px;height: 36px; min-height: 36px;'>
        <div class="m-container m-container--fluid m-container--full-height m-page__container">
            <div class="m-stack m-stack--flex-tablet-and-mobile m-stack--ver m-stack--desktop">
                <div class="m-stack__item m-stack__item--left m-stack__item--middle m-stack__item--last">
                        <span class="m-footer__copyright">
                            2019 &copy; 锦E行管理门户 by <a href="#" class="m-link">苏州农商银行</a>
                        </span>
                </div>
                <div class="m-stack__item m-stack__item--right m-stack__item--middle m-stack__item--first">

                </div>
            </div>
        </div>
    </footer>
    <!-- end::Footer -->
</div>

<!-- begin::Scroll Top -->
<div id="m_scroll_top" class="m-scroll-top">
    <i class="la la-arrow-up"></i>
</div>

<script src="${fep}/vendors/jquery/dist/jquery.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/popper.js/dist/umd/popper.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap/dist/js/bootstrap.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js-cookie/src/js.cookie.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/moment/min/moment.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/tooltip.js/dist/umd/tooltip.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/perfect-scrollbar/dist/perfect-scrollbar.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/wnumb/wNumb.js?version=1.0.0.0.1" type="text/javascript"></script>
<!--end:: Global Mandatory Vendors -->

<!--begin:: Global Optional Vendors -->
<script src="${fep}/vendors/jquery.repeater/src/lib.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/jquery.repeater/src/jquery.input.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/jquery.repeater/src/repeater.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/jquery-form/dist/jquery.form.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/block-ui/jquery.blockUI.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/forms/bootstrap-datepicker.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-datetime-picker/js/bootstrap-datetimepicker.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-timepicker/js/bootstrap-timepicker.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/forms/bootstrap-timepicker.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-daterangepicker/daterangepicker.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/forms/bootstrap-daterangepicker.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-maxlength/src/bootstrap-maxlength.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-switch/dist/js/bootstrap-switch.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/forms/bootstrap-switch.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/./vendors/bootstrap-multiselectsplitter/bootstrap-multiselectsplitter.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-select/dist/js/bootstrap-select.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/select2/dist/js/select2.full.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/select2/dist/js/i18n/zh-CN.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/typeahead.js/dist/typeahead.bundle.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/handlebars/dist/handlebars.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/inputmask/dist/jquery.inputmask.bundle.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/inputmask/dist/inputmask/inputmask.date.extensions.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/inputmask/dist/inputmask/inputmask.numeric.extensions.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/inputmask/dist/inputmask/inputmask.phone.extensions.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/nouislider/distribute/nouislider.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/owl.carousel/dist/owl.carousel.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/autosize/dist/autosize.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/clipboard/dist/clipboard.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/ion-rangeslider/js/ion.rangeSlider.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/dropzone/dist/dropzone.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/summernote/dist/summernote.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/summernote/dist/lang/summernote-zh-CN.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/markdown/lib/markdown.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-markdown/js/bootstrap-markdown.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/forms/bootstrap-markdown.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/jquery-validation/dist/jquery.validate.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/jquery-validation/dist/additional-methods.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/forms/jquery-validation.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-notify/bootstrap-notify.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/base/bootstrap-notify.init.js" type="text/javascript"></script>
<script src="${fep}/vendors/toastr/build/toastr.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/jstree/dist/jstree.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/raphael/raphael.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/morris.js/morris.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/chartist/dist/chartist.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/chart.js/dist/Chart.bundle.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/charts/chart.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/./vendors/bootstrap-session-timeout/dist/bootstrap-session-timeout.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/./vendors/jquery-idletimer/idle-timer.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/waypoints/lib/jquery.waypoints.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/counterup/jquery.counterup.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/es6-promise-polyfill/promise.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/sweetalert2/dist/sweetalert2.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/js/framework/components/plugins/base/sweetalert2.init.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/picture/js/pictureViewer.js?version=1.0.0.0.1"></script>
<!--end:: Global Optional Vendors -->

<script src="${fep}/vendors/bootstrap-fileinput/js/fileinput.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-fileinput/js/locales/zh.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-fileinput/themes/fa/theme.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/vendors/bootstrap-datetime-picker/js/bootstrap-datetimepicker.zh-CN.js?version=1.0.0.0.1" type="text/javascript"></script>
<!--begin::Global Theme Bundle -->
<script src="${fep}/assets/demo/base/scripts.bundle.js?version=1.0.0.0.2" type="text/javascript"></script>
<script src="${fep}/assets/./vendors/custom/datatables/datatables.bundle.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/assets/./vendors/custom/fixedcolumns/dataTables.fixedColumns.min.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/assets/./vendors/custom/jquery-ui/jquery-ui.bundle.js?version=1.0.0.0.1" type="text/javascript"></script>
<script> document.write('<script  src="${fep}/assets/app/js/common.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/common_app.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/common_menu.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/common_resource.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/common_user.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/process_data.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/DACIA.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script src="${fep}/assets/app/js/portal.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/assets/app/js/form.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/assets/app/js/dateUtil.js?version=1.0.0.0.1" type="text/javascript"></script>
<script src="${fep}/assets/app/js/collection.js?version=1.0.0.0.1" type="text/javascript"></script>
<script> document.write('<script  src="${fep}/assets/app/js/jstreeUtil.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script> document.write('<script  src="${fep}/assets/app/js/fileinputUtil.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script src="${fep}/assets/app/js/checktask.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/handleStatus.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/crtDepcode.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/codetransfer.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/dataclone.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/compont.js?version=1.0.0.0.1"></script>
<script src="${fep}/vendors/jtopo/jtopo-0.4.8.js?version=1.0.0.0.1"></script>
<script src="${fep}/vendors/printarea/jquery.PrintArea.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/common_portal.js?version=1.0.0.0.2"></script>
<script src="${fep}/assets/app/js/browser.js?version=1.0.0.0.1"></script>
<script src="${fep}/assets/app/js/checkinput.js?version=1.0.0.0.1"></script>
<script>document.write('<script  src="${fep}/assets/app/js/treecontrol.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script>document.write('<script  src="${fep}/assets/app/js/util.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script src="${fep}assets/app/js/core.js" type="text/javascript"></script>
<script src="${fep}assets/app/js/crypto-js.js" type="text/javascript"></script>
<script src="${fep}assets/app/js/aes.js" type="text/javascript"></script>
<script src="${fep}assets/app/js/mode-ecb.js" type="text/javascript"></script>
<script src="${fep}assets/app/js/jsencrypt.js" type="text/javascript"></script>
<script>document.write('<script  src="${fep}/assets/app/js/convertrsa.js?v=' + new Date().getTime() + ' " type="text/javascript" ><\/script>')</script>
<script>
    $(window).on('load', function () {
        DACIA.init();
        var pageLoading = self.setInterval(function () {
            if (DACIARuntime.loadReady >= 7) {
                $('body').removeClass('m-page--loading');
                PortletDraggable.init();
                DACIARuntime.initial = 1;
                window.clearInterval(pageLoading);
            }
        }, 2000);
        //重要通知
        history.pushState(null, null, "#");
        window.addEventListener('popstate', function () {
            history.pushState(null, null, "#");
        })
    });
</script>
</body>
</html>