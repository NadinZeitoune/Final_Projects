import 'package:cloud_firestore/cloud_firestore.dart' as cloud_firestore;

class InstagramUser {
  String? imgUrl;
  String? name;
  String? bio;
  String? userName;
  String? website;

  InstagramUser(
      {this.imgUrl, this.name, this.bio, this.userName, this.website});

  factory InstagramUser.fromFirestoreUser(
      cloud_firestore.DocumentSnapshot userSnapShot) {
    return InstagramUser(
        imgUrl: userSnapShot['profileImageUrl'],
        name: userSnapShot['name'],
        bio: userSnapShot['bio'],
        userName: userSnapShot['userName'],
        website: userSnapShot['website']);
  }
}
