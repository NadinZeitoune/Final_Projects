// @CupertinoAutoRouter
// @AdaptiveAutoRouter
// @CustomAutoRouter
import 'package:auto_route/auto_route.dart';
import 'package:flutter_instagram/feedF/presentation/pages/feed_page.dart';
import 'package:flutter_instagram/loginF/presentation/pages/login_page.dart';
import 'package:flutter_instagram/loginF/presentation/pages/register_profile_page.dart';
import 'package:flutter_instagram/loginF/presentation/pages/splash_screen_page.dart';

// Todo: After change please run:
// flutter packages pub run build_runner watch

@MaterialAutoRouter(
  replaceInRouteName: 'Page,Route',
  routes: <AutoRoute>[
    AutoRoute(page: SplashScreenPage, initial: true), // Initial == first screen
    AutoRoute(page: LoginPage),
    AutoRoute(page: RegisterProfilePage),
    AutoRoute(page: FeedPage),
  ],
)
class $InstagramRouter {}