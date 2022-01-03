// Logo
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

class InstagramLogo extends StatelessWidget {
  final double? width;
  final double? height;

  const InstagramLogo({Key? key, this.width, this.height}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width ?? 200,
      height: height ?? 100,
      child: SvgPicture.asset("lib/core/assets/instagram_logo.svg"),
    );
  }
}
