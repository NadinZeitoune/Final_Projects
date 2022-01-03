import 'package:auto_route/auto_route.dart';
import 'package:dartz/dartz.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_instagram/core/di/di.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:flutter_instagram/feedF/presentation/pages/feed_page.dart';
import 'package:flutter_instagram/loginF/core/failures.dart';
import 'package:flutter_instagram/loginF/core/shared_prefs_names.dart';
import 'package:flutter_instagram/loginF/presentation/pages/register_profile_page.dart';
import 'package:flutter_instagram/routes/routes.gr.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginProvider with ChangeNotifier {
  final GoogleSignIn googleSignIn;
  final FirebaseAuth _auth;
  final InstagramUser currentLoggedInUser;// = InstagramUser();
  //GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  LoginProvider()
      : _auth = FirebaseAuth.instance,
        googleSignIn = GoogleSignIn(),
  currentLoggedInUser = getIt();

  Future<Either<InstagramUser, LoginFailure>> _signInWithGoogle() async {
    await Firebase.initializeApp();

    final GoogleSignInAccount googleSignInAccount = (await googleSignIn.signIn())!;
    final GoogleSignInAuthentication googleSignInAuthentication =
        await googleSignInAccount.authentication;

    final AuthCredential credential = GoogleAuthProvider.credential(
      accessToken: googleSignInAuthentication.accessToken,
      idToken: googleSignInAuthentication.idToken,
    );

    UserCredential authResult;
    User? user;

    if (_auth.currentUser == null) {
      authResult = await _auth.signInWithCredential(credential);
      user = authResult.user;
    } else {
      user = _auth.currentUser!;
    }

    if (user != null) {
      assert(!user.isAnonymous);
      assert(await user.getIdToken() != null);

      final User currentUser = _auth.currentUser!;
      assert(user.uid == currentUser.uid);

      //currentLoggedInUser.uid = user.uid;

      print('signInWithGoogle succeeded: $user');

      return Left(currentLoggedInUser);
    }

    return Right(LoginFailure());
  }

  Future signInWithGoogle() async {
    Either<InstagramUser, LoginFailure> result = await _signInWithGoogle();

    result.fold(
      (InstagramUser user) async {
        SharedPreferences prefs = await SharedPreferences.getInstance();

        //if (prefs.getBool(IS_REGISTERED) != null) {//To work on register(after first registration!)
        if (prefs.getBool(IS_REGISTERED) == null) {//To work on feed

          /*Navigation*/
          navigateToRegisterProfilePage();
        } else {
          navigateToFeedPage();
        }
      },
      (LoginFailure failure) {
        /*Pop up*/
        print("login failed");
      },
    );
  }

  void signOutGoogle() async {
    await googleSignIn.signOut();

    print("User Signed Out");
  }

  void navigateToRegisterProfilePage() {
    //RegisterProfilePage()
    /*navigatorKey.currentState.pushReplacement(
        MaterialPageRoute(builder: (context) => RegisterProfilePage()));*/
    //getIt<InstagramRouter>().popAndPush(const RegisterProfileRoute()); // popAndPush - not working. Use replace v
    getIt<InstagramRouter>().replace(const RegisterProfileRoute());
  }

  void navigateToFeedPage() {
    /*navigatorKey.currentState
        .pushReplacement(MaterialPageRoute(builder: (context) => FeedPage()));*/
    //getIt<InstagramRouter>().popAndPush(const FeedRoute()); // popAndPush - not working. Use replace v
    getIt<InstagramRouter>().replace(const FeedRoute());
  }
}
