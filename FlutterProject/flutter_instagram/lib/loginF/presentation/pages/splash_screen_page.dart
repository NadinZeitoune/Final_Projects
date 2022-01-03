import 'package:dartz/dartz.dart';
import 'package:flutter/material.dart';
import 'package:flutter_instagram/core/di/di.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:flutter_instagram/core/widgets/instagram_logo.dart';
import 'package:flutter_instagram/loginF/core/failures.dart';
import 'package:flutter_instagram/loginF/presentation/state_management/login_provider.dart';
import 'package:flutter_instagram/routes/routes.gr.dart';
import 'package:provider/provider.dart';

class SplashScreenPage extends StatelessWidget {
  const SplashScreenPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    getIt
        .getAsync<Either<InstagramUser, UserNotFoundFailure>>()
        .then((userOrFail) => {
              userOrFail.fold(
                  (instagramUser) => {
                        Provider.of<LoginProvider>(context, listen: false)
                            .signInWithGoogle()
                      },
                  (userNotFoundFailure) =>
                      {getIt<InstagramRouter>().replace(const LoginRoute())})
            });

    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Image.asset(
              'lib/core/assets/instagram_app_splash.png',
              width: 100,
            ),
            InstagramLogo(),
          ],
        ),
      ),
    );
  }
}
