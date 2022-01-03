// **************************************************************************
// AutoRouteGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouteGenerator
// **************************************************************************

import 'package:auto_route/auto_route.dart' as _i5;
import 'package:flutter/material.dart' as _i6;

import '../feedF/presentation/pages/feed_page.dart' as _i4;
import '../loginF/presentation/pages/login_page.dart' as _i2;
import '../loginF/presentation/pages/register_profile_page.dart' as _i3;
import '../loginF/presentation/pages/splash_screen_page.dart' as _i1;

class InstagramRouter extends _i5.RootStackRouter {
  InstagramRouter([_i6.GlobalKey<_i6.NavigatorState>? navigatorKey])
      : super(navigatorKey);

  @override
  final Map<String, _i5.PageFactory> pagesMap = {
    SplashScreenRoute.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
          routeData: routeData, child: const _i1.SplashScreenPage());
    },
    LoginRoute.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
          routeData: routeData, child: const _i2.LoginPage());
    },
    RegisterProfileRoute.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
          routeData: routeData, child: const _i3.RegisterProfilePage());
    },
    FeedRoute.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
          routeData: routeData, child: const _i4.FeedPage());
    }
  };

  @override
  List<_i5.RouteConfig> get routes => [
        _i5.RouteConfig(SplashScreenRoute.name, path: '/'),
        _i5.RouteConfig(LoginRoute.name, path: '/login-page'),
        _i5.RouteConfig(RegisterProfileRoute.name,
            path: '/register-profile-page'),
        _i5.RouteConfig(FeedRoute.name, path: '/feed-page')
      ];
}

/// generated route for
/// [_i1.SplashScreenPage]
class SplashScreenRoute extends _i5.PageRouteInfo<void> {
  const SplashScreenRoute() : super(SplashScreenRoute.name, path: '/');

  static const String name = 'SplashScreenRoute';
}

/// generated route for
/// [_i2.LoginPage]
class LoginRoute extends _i5.PageRouteInfo<void> {
  const LoginRoute() : super(LoginRoute.name, path: '/login-page');

  static const String name = 'LoginRoute';
}

/// generated route for
/// [_i3.RegisterProfilePage]
class RegisterProfileRoute extends _i5.PageRouteInfo<void> {
  const RegisterProfileRoute()
      : super(RegisterProfileRoute.name, path: '/register-profile-page');

  static const String name = 'RegisterProfileRoute';
}

/// generated route for
/// [_i4.FeedPage]
class FeedRoute extends _i5.PageRouteInfo<void> {
  const FeedRoute() : super(FeedRoute.name, path: '/feed-page');

  static const String name = 'FeedRoute';
}
