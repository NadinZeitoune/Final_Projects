import 'package:auto_route/auto_route.dart';
import 'package:dartz/dartz.dart';
import 'package:flutter/material.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:flutter_instagram/core/widgets/instagram_logo.dart';
import 'package:flutter_instagram/loginF/core/failures.dart';
import 'package:flutter_instagram/loginF/presentation/state_management/login_provider.dart';
import 'package:flutter_signin_button/flutter_signin_button.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:provider/provider.dart';

class LoginPage extends StatelessWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            SizedBox(height: MediaQuery.of(context).size.height * 0.1),
            InstagramLogo(),
            SizedBox(height: MediaQuery.of(context).size.height * 0.2),
            LoginWithGoogle(),
          ],
        ),
      ),
    );
  }
}

class LoginWithGoogle extends StatelessWidget {
  const LoginWithGoogle({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SignInButton(
      Buttons.Google,
      elevation: 5,
      text: "Sign up with Google",
      onPressed: () async {
        //context.read<LoginProvider>().signInWithGoogle();
        await Provider.of<LoginProvider>(context, listen: false)
            .signInWithGoogle();
      },
    );
  }
}
