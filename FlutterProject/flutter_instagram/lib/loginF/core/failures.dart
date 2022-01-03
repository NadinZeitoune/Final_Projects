import 'package:flutter_instagram/core/failures.dart';

class LoginFailure extends Failures{

  LoginFailure(){
    print("Login failure");
  }
}

class UserNotFoundFailure extends Failures{

  UserNotFoundFailure(){
    print("UserNotFound failure");
  }
}

class FirebaseCommunicationFailure extends Failures{

  FirebaseCommunicationFailure(){
  print("FirebaseCommunication failure");
}
}