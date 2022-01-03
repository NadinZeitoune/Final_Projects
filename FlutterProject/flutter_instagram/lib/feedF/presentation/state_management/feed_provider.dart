import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_instagram/core/constants/feed_configuration.dart';
import 'package:flutter_instagram/core/di/di.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:flutter_instagram/feedF/domain/entities/post.dart';
import 'package:flutter_instagram/routes/routes.gr.dart';
import 'package:google_sign_in/google_sign_in.dart';

class FeedProvider extends ChangeNotifier {
  final InstagramUser currentLoggedInUser;
  final GoogleSignIn googleSignIn;

  int postsCounter = 0;
  int refreshCounter = 0;
  List<Post> currentPosts = [];

  final StreamController<List<Post>> _currentPostsList =
      StreamController<List<Post>>.broadcast();

  Stream<List<Post>> get currentPostsStreamList => _currentPostsList.stream;

  FeedProvider()
      : googleSignIn = getIt(),
        currentLoggedInUser = getIt.get<InstagramUser>(); // Same as ^

  void signOutGoogle() async {
    await googleSignIn.signOut();
    navigateToLoginPage();
    print("User Signed Out");
  }

  Future refreshFeed() async {
    refreshCounter++;
    postsCounter = 0;
    currentPosts.clear();

    callToFireBaseFunctionAlgorithm();
    bool isNewPostsArrive = await listenNewDataAdded();

    if (!isNewPostsArrive) {
      // You can show connection error or other error here.
      print("No result");
    }
  }

  Future<bool> listenNewDataAdded() async{
    return await _currentPostsList.stream.any((postsList){
      return postsList.length > 0;
    });
  }


  void navigateToLoginPage() {
    getIt<InstagramRouter>().replace(const LoginRoute());
    // TODO: Does it need to clear the prefs?
  }


  // FAKE backend: ---------------------------------------------------

  void callToFireBaseFunctionAlgorithm() async {
    await Future.delayed(Duration(seconds: 2));
    await fetchMorePosts();
  }

  Future fetchMorePosts(
      {int fetchedPosts = FeedConfiguration.POST_PER_LOAD}) async {
    for (int i = 0; i < fetchedPosts; i++) {
      currentPosts.add(
        Post(
          photoUrl:
              "https://cdn.pixabay.com/photo/2016/11/18/15/46/birthday-1835443__340.jpg",
              //"https://ehire.co.za/wp-content/uploads/2020/01/birthday-party-ideas-portrait-of-happy-birthday-boy.jpg",
          description: "${postsCounter++}, refresh $refreshCounter",
        ),
      );
    }

    _currentPostsList.sink.add(currentPosts);
  }
}
