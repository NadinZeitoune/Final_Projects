import 'dart:async';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_instagram/core/constants/feed_configuration.dart';
import 'package:flutter_instagram/core/di/di.dart';
import 'package:flutter_instagram/core/entities/user.dart';
import 'package:flutter_instagram/core/widgets/instagram_logo.dart';
import 'package:flutter_instagram/feedF/domain/entities/post.dart';
import 'package:flutter_instagram/feedF/presentation/state_management/feed_provider.dart';
import 'package:flutter_instagram/feedF/presentation/state_management/post_provider.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:provider/provider.dart';

class FeedPage extends StatefulWidget {
  const FeedPage({Key? key}) : super(key: key);

  @override
  State<FeedPage> createState() => _FeedPageState();
}

class _FeedPageState extends State<FeedPage> {
  InstagramUser user;

  _FeedPageState() : user = getIt.get<InstagramUser>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        title: Row(
          children: [
            ClickableAsset(
              assetPath: 'lib/core/assets/feed_assets/camera.svg',
              onTap: () {},
              width: 30,
              paddingLeft: 10,
            ),
            InstagramLogo(height: 50),
          ],
        ),
        leadingWidth: 0,
        actions: [
          ClickableAsset(
            assetPath: 'lib/core/assets/feed_assets/send_message.svg',
            onTap: () {},
            width: 30,
            paddingRight: 10,
            paddingLeft: 10,
          )
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: Center(
              child: RefreshIndicator(
                onRefresh: Provider.of<FeedProvider>(context, listen: false)
                    .refreshFeed,
                child: ListOfPosts(),
              ),
            ),
          ),
          FeedBottomAppBar(),
        ],
      ),
    );
  }
}

class ListOfPosts extends StatefulWidget {
  const ListOfPosts({Key? key}) : super(key: key);

  @override
  State<ListOfPosts> createState() => _ListOfPostsState();
}

class _ListOfPostsState extends State<ListOfPosts> {
  StreamController<Post> streamController = StreamController<Post>();
  bool generateNew = false;
  late ScrollController _controller;

  @override
  void initState() {
    super.initState();
    _controller = ScrollController();
    _controller.addListener(_scrollListener);
  }

  @override
  void dispose() {
    streamController.close();
    super.dispose();
  }

  _scrollListener() {
    if (_controller.offset >= _controller.position.maxScrollExtent &&
        !_controller.position.outOfRange) {
      // Reach the bottom callback
    }
    if (_controller.offset <= _controller.position.minScrollExtent &&
        !_controller.position.outOfRange) {
      // Reach the top callback
    }
  }

  @override
  Widget build(BuildContext context) {
    FeedProvider _feedProvider =
        Provider.of<FeedProvider>(context, listen: false);

    return StreamBuilder<List<Post>>(
      stream: _feedProvider.currentPostsStreamList,
      initialData: [],
      builder: (BuildContext context, AsyncSnapshot snapShot) {
        if (!snapShot.hasData) {
          return const Text("no Connection");
        }

        if (snapShot.connectionState == ConnectionState.active) {
          return ListView.builder(
            controller: _controller,
            itemCount: snapShot.data.length + 1,
            itemBuilder: (context, index) {
              print("--------------building post number $index");

              if (snapShot.data.length -
                      FeedConfiguration.START_NEW_LOAD_ON_POSTS_LEFT ==
                  index) {
                print("PostsListLength ${_feedProvider.currentPosts.length}");
                print("Snapshot is: ${snapShot.data.length}");
                _feedProvider.callToFireBaseFunctionAlgorithm();
              }

              if (snapShot.data.length == index) {
                return Center(child: CircularProgressIndicator());
              }

              return ChangeNotifierProvider(
                key: ObjectKey(snapShot.data[index]), // || //UniqueKey(),
                create: (context) => PostProvider(post: snapShot.data[index]),
                child: ListPostTile(),
              );
            },
          );
        } else {
          _feedProvider.callToFireBaseFunctionAlgorithm();
          return const Text("no posts yet...");
        }
      },
    );
  }
}

class ListPostTile extends StatefulWidget {
  const ListPostTile({Key? key}) : super(key: key);

  @override
  _ListPostTileState createState() => _ListPostTileState();
}

class _ListPostTileState extends State<ListPostTile> {
  @override
  Widget build(BuildContext context) {
    return Consumer<PostProvider>(
      builder: (context, postProvider, child) {
        return ListTile(
          key: UniqueKey(),
          title: Padding(
            padding: const EdgeInsets.symmetric(vertical: 20),
            child: Column(
              children: [
                Stack(
                  alignment: Alignment.center,
                  children: [
                    CachedNetworkImage(
                      placeholder: (context, url) =>
                          CircularProgressIndicator(),
                      imageUrl: postProvider.post.photoUrl,
                    ),
                    Container(
                      color: Color.fromARGB(170, 0, 0, 0),
                      width: 300,
                      child: Center(
                        child: Text(
                          postProvider.post.description!,
                          textScaleFactor: 2,
                          style: TextStyle(color: Colors.white),
                        ),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 10),
                Row(
                  children: [
                    ClickableAsset(
                      assetPath: postProvider.isClicked
                          ? postProvider.likePath
                          : postProvider.likeClickedPath,
                      onTap: postProvider.like,
                      width: 25,
                      paddingRight: 10,
                    ),
                    ClickableAsset(
                      assetPath:
                          'lib/core/assets/feed_assets/write_comment.svg',
                      onTap: () {},
                      width: 25,
                      paddingRight: 10,
                    ),
                  ],
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}

class FeedBottomAppBar extends StatelessWidget {
  const FeedBottomAppBar({Key? key}) : super(key: key);
  final double btmAppBarIconsW = 30;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 50,
      color: Colors.white,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            ClickableAsset(
              assetPath: 'lib/core/assets/feed_assets/home_clicked.svg',
              onTap: () {},
              width: btmAppBarIconsW,
              paddingRight: 10,
              paddingLeft: 10,
            ),
            ClickableAsset(
              assetPath: 'lib/core/assets/feed_assets/search.svg',
              onTap: () {},
              width: btmAppBarIconsW,
              paddingRight: 10,
              paddingLeft: 10,
            ),
            ClickableAsset(
              assetPath: 'lib/core/assets/feed_assets/add.svg',
              onTap: () {},
              width: btmAppBarIconsW,
              paddingRight: 10,
              paddingLeft: 10,
            ),
            ClickableAsset(
              assetPath: 'lib/core/assets/feed_assets/like.svg',
              onTap: () {},
              width: btmAppBarIconsW,
              paddingRight: 10,
              paddingLeft: 10,
            ),
            ClickableAsset(
              assetPath: 'lib/core/assets/feed_assets/send_message.svg',
              onTap: () {},
              width: btmAppBarIconsW,
              paddingRight: 10,
              paddingLeft: 10,
            ),
          ],
        ),
      ),
    );
  }
}

class ClickableAsset extends StatelessWidget {
  final String assetPath;
  final void Function() onTap;
  final double width;
  final double? paddingRight;
  final double? paddingLeft;

  const ClickableAsset({
    Key? key,
    required this.assetPath,
    required this.onTap,
    required this.width,
    this.paddingRight,
    this.paddingLeft,
  })  : assert(width > 0),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      // Without visuals
      child: Padding(
        //padding: const EdgeInsets.symmetric(horizontal: 10),
        padding: EdgeInsets.only(right: paddingRight ?? 0.0, left: 10),
        child: SvgPicture.asset(
          this.assetPath,
          width: this.width,
        ),
      ),
      onTap: this.onTap,
    );
  }
}
