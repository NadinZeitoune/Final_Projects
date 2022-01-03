import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:flutter_instagram/core/di/di.dart';
import 'package:flutter_instagram/feedF/presentation/state_management/feed_provider.dart';
import 'package:flutter_instagram/loginF/presentation/pages/login_page.dart';
import 'package:flutter_instagram/loginF/presentation/state_management/login_provider.dart';
import 'package:flutter_instagram/loginF/presentation/state_management/register_profile_provider.dart';
import 'package:flutter_instagram/routes/routes.gr.dart';
import 'package:provider/provider.dart';

class InstagramApp extends StatelessWidget {
  //final GlobalKey<NavigatorState> _mainNavigatorKey = GlobalKey<NavigatorState>();

  InstagramApp({Key? key}) : super(key: key);
  final _appRouter = getIt<InstagramRouter>();//InstagramRouter();

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
        providers: [
          ChangeNotifierProvider<LoginProvider>(create: (_) => LoginProvider()),
          ChangeNotifierProvider<RegisterProfileProvider>(
              create: (_) => RegisterProfileProvider()),
          ChangeNotifierProvider<FeedProvider>(create: (_) => FeedProvider()),
        ],
        child: MaterialApp.router(
            routeInformationParser: _appRouter.defaultRouteParser(),
            routerDelegate: AutoRouterDelegate(_appRouter))
        /*MaterialApp(
        //navigatorKey: _mainNavigatorKey,
        theme: ThemeData.light(),
        home: LoginPage(),
        ),*/
        );
  }
}
