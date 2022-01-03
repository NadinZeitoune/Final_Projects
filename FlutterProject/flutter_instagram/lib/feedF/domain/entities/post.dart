import 'package:equatable/equatable.dart';

class Post extends Equatable{

  final String? description;
  final String photoUrl;

  Post({this.description, required this.photoUrl});

  @override
  List<Object> get props => [description!, description!];
}