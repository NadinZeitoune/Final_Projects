import 'package:flutter/cupertino.dart';
import 'package:flutter_instagram/feedF/domain/entities/post.dart';

class PostProvider extends ChangeNotifier{
  final String likePath = 'lib/core/assets/feed_assets/like.svg';
  final String likeClickedPath = 'lib/core/assets/feed_assets/like_clicked.svg';

  Post post;
  late bool isClicked;

  PostProvider({required this.post}){

    this.isClicked = true;
  }

  void like(){
    this.isClicked = true;
    notifyListeners();
  }
}