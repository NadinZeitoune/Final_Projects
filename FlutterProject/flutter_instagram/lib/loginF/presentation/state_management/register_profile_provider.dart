import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:dartz/dartz.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:flutter_instagram/core/di/di.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:cloud_firestore/cloud_firestore.dart' as cloud_firestore;
import 'package:firebase_storage/firebase_storage.dart' as firebase_storage;
import 'package:flutter_instagram/core/firebase_entities/collections.dart';
import 'package:flutter_instagram/loginF/core/failures.dart';
import 'package:flutter_instagram/loginF/core/shared_prefs_names.dart';
import 'package:flutter_instagram/loginF/core/success.dart';
import 'package:flutter_instagram/routes/routes.gr.dart';
import 'dart:io' as io;

import 'package:image_picker/image_picker.dart';
import 'package:shared_preferences/shared_preferences.dart';

class RegisterProfileProvider with ChangeNotifier {
  final InstagramUser currentLoggedInUser;
  final GlobalKey<FormBuilderState> fbKey = GlobalKey<FormBuilderState>();
  io.File? imageFile;

  RegisterProfileProvider() : currentLoggedInUser = getIt.get<InstagramUser>();

  Future<void> registerComplete() async {
    if (fbKey.currentState!.saveAndValidate()) {
      print(fbKey.currentState!.value);

      currentLoggedInUser.name = fbKey.currentState!.value["Name"];
      currentLoggedInUser.website = fbKey.currentState!.value["Website"];
      currentLoggedInUser.bio = fbKey.currentState!.value["Bio"];
      currentLoggedInUser.userName = fbKey.currentState!.value["Username"];

      Either<FirebaseDataSaved, FirebaseCommunicationFailure> savedResult =
          await saveUserOnFireStore(currentLoggedInUser);

      savedResult.fold((success) async {
        SharedPreferences prefs = await SharedPreferences.getInstance();
        await prefs.setBool(IS_REGISTERED, true);
        navigateToFeedPage();
      }, (failed) {});
      /*.then((value) =>
              print("----------------------------------------User Added"))
          .catchError((error) => print(
              "----------------------------------------Failed to add user: $error"));*/
    }
  }

  void navigateToFeedPage() {
    getIt<InstagramRouter>().replace(const FeedRoute());
  }

  Future<Either<FirebaseDataSaved, FirebaseCommunicationFailure>>
      saveUserOnFireStore(InstagramUser userToSave) async {
    // Create a CollectionReference called users that references the fireStore collection
    //CollectionReference users = FirebaseFirestore.instance.collection('Users');
    cloud_firestore.CollectionReference users =
        getIt.get<UsersCollection>().collectionReference;
    //cloud_firestore.FirebaseFirestore.instance.collection('Users');^

    try {
      await users.doc(FirebaseAuth.instance.currentUser!.uid).set({
        'name': userToSave.name,
        'bio': userToSave.bio,
        'profileImageUrl': userToSave.imgUrl,
        'userName': userToSave.userName,
        'website': userToSave.website,
      });
    } catch (err) {
      print('Save user failed $err');
      return Right(FirebaseCommunicationFailure());
    }

    if (imageFile != null) {
      await updateUserProfilePhoto(await uploadFile(imageFile!));
    }

    return Left(FirebaseDataSaved());
  }

  Future handleUploadType() async {
    XFile? pickedImage =
        await ImagePicker().pickImage(source: ImageSource.gallery);

    if (pickedImage != null) {
      imageFile = io.File(pickedImage.path);
    }

    notifyListeners();
  }

  Future updateUserProfilePhoto(
      firebase_storage.UploadTask photoTaskCompleted) async {
    String photoUrl = await photoTaskCompleted.snapshot.ref.getDownloadURL();

    currentLoggedInUser.imgUrl = photoUrl;

    await cloud_firestore.FirebaseFirestore.instance
        .collection('Users')
        .doc(FirebaseAuth.instance.currentUser!.uid)
        .update({'profileImageUrl': photoUrl});
  }

  Future<firebase_storage.UploadTask> uploadFile(io.File file) async {
    firebase_storage.UploadTask uploadTask;
    firebase_storage.Reference ref = firebase_storage.FirebaseStorage.instance
        .ref()
        .child('users_private_media')
        .child('/profile-image-${FirebaseAuth.instance.currentUser!.uid}.jpg');

    final metadata = firebase_storage.SettableMetadata(
      contentType: 'image/jpeg',
      customMetadata: {
        'picked-file-path': file.path,
        'user-id': FirebaseAuth.instance.currentUser!.uid,
      },
    );

    if (kIsWeb) {
      uploadTask = ref.putData(await file.readAsBytes(), metadata);
    } else {
      uploadTask = ref.putFile(io.File(file.path), metadata);
    }

    return Future.value(uploadTask);
  }
}
