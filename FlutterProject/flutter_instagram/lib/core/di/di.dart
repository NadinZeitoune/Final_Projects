import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:dartz/dartz.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_instagram/core/firebase_entities/collections.dart';
import 'package:get_it/get_it.dart';
import 'package:flutter_instagram/routes/routes.gr.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:flutter_instagram/loginF/core/failures.dart';
import 'package:google_sign_in/google_sign_in.dart';

final getIt = GetIt.instance;

void setup() {
  // Navigation
  getIt.registerSingleton<InstagramRouter>(InstagramRouter());

  // To do sign in & sign out from everywhere in the app.
  getIt.registerLazySingleton(() => GoogleSignIn());

  // Currant user - check if the user is logged in.
  //getIt.registerSingleton(InstagramUser());
  //getIt.registerSingleton(() => InstagramUser());// Same as ^
  getIt.registerLazySingletonAsync<Either<InstagramUser, UserNotFoundFailure>>(
      () async {
    await Future.delayed(Duration(seconds: 2));
    await Firebase.initializeApp();
    //cloud_firestore.FirebaseFirestore.instance.collection('Users');
    try {
      DocumentSnapshot documentSnapshot = await getIt
          .get<UsersCollection>()
          .collectionReference
          .doc(FirebaseAuth.instance.currentUser!.uid)
          .get();

      getIt.registerLazySingleton(
          () => InstagramUser.fromFirestoreUser(documentSnapshot));

      return Left(getIt.get<InstagramUser>());
    } catch (err) {
      print("error accrued $err");
      getIt.registerLazySingleton(() => InstagramUser());
      return Right(UserNotFoundFailure());
    }
  });

  // To get the names of the fireStore collections.
  getIt.registerLazySingleton(() => UsersCollection());
}
