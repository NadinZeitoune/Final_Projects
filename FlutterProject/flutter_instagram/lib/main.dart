//import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'instagram.dart';
import 'package:flutter_instagram/core/di/di.dart' as di;

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  /*await Firebase.initializeApp(
      options: FirebaseOptions(
          appId: "appId",
          apiKey: "apiKey",
          messagingSenderId: "messagingSenderId",
          projectId: "instagram-7f452"))*/
  await Firebase.initializeApp();
  //await FirebaseAuth.instance.useAuthEmulator('localhost', 9099);

  // To clear any shared prefs:
  /*SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.clear();*/

  // Dependency Injection set up
  di.setup();

  runApp(InstagramApp());
}
