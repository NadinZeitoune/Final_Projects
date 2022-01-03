import 'package:cloud_firestore/cloud_firestore.dart' as cloud_firestore;


class Collection {
  cloud_firestore.CollectionReference? _ref;
  cloud_firestore.CollectionReference get collectionReference => this._ref!;
}

class UsersCollection extends Collection{
  UsersCollection(){
    this._ref = cloud_firestore.FirebaseFirestore.instance.collection('Users');
  }
}