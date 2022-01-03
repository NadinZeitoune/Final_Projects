import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:flutter_instagram/core/widgets/instagram_logo.dart';
import 'package:flutter_instagram/loginF/presentation/state_management/register_profile_provider.dart';
import 'package:provider/provider.dart';

class RegisterProfilePage extends StatelessWidget {
  const RegisterProfilePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return FormBuilder(
      key: Provider.of<RegisterProfileProvider>(context, listen: false).fbKey,
      initialValue: {},
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.white,
          title: InstagramLogo(
            height: 50,
          ),
          centerTitle: true,
        ),
        body: Container(
          child: Column(
            children: [
              PhotoUploader(),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: TextFieldsEditor(),
              ),
              MaterialButton(
                  child: Text("Save"),
                  onPressed: Provider.of<RegisterProfileProvider>(context,
                          listen: false)
                      .registerComplete),
            ],
          ),
        ),
      ),
    );
  }
}

class TextFieldsEditor extends StatelessWidget {
  const TextFieldsEditor({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        InstagramTextFields(name: "Name"),
        InstagramTextFields(name: "Username"),
        InstagramTextFields(name: "Bio", multiline: true),
        InstagramTextFields(name: "Website"),
      ],
    );
  }
}

class InstagramTextFields extends StatelessWidget {
  final bool multiline;
  final String name;

  const InstagramTextFields(
      {Key? key, required this.name, this.multiline = false})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(flex: 1, child: Text(this.name)),
        if (this.multiline) ...{
          Expanded(
              flex: 3,
              child: FormBuilderTextField(
                keyboardType: TextInputType.multiline,
                minLines: 1, //Normal textInputField will be displayed
                maxLines: 5, // when user presses enter it will adapt to it
                name: name,
              )),
        } else ...{
          Expanded(
              flex: 3,
              child: FormBuilderTextField(
                name: name,
              ))
        },
      ],
    );
  }
}

class PhotoUploader extends StatelessWidget {
  const PhotoUploader({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.black12,
      width: MediaQuery.of(context).size.width,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          SizedBox(height: 10),
          InkWell(
            child: CirclePhoto(),
            onTap: () async => {
              await Provider.of<RegisterProfileProvider>(context, listen: false)
                  .handleUploadType()
            },
          ),
          SizedBox(height: 10),
          InkWell(
            child: Text(
              'Upload Profile Photo',
              style: TextStyle(color: Colors.blue),
            ),
            onTap: () async => {
              await Provider.of<RegisterProfileProvider>(context, listen: false)
                  .handleUploadType()
            },
          ),
          SizedBox(height: 10),
        ],
      ),
    );
  }
}

class CirclePhoto extends StatelessWidget {
  final String? url;
  final double radius = 80;
  final Color circleEmptyColor = Colors.white;

  const CirclePhoto({Key? key, this.url}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<RegisterProfileProvider>(
        builder: (context, registerProfileProvider, child) {
      if (registerProfileProvider.imageFile == null) {
        return CircleAvatar(
            backgroundColor: circleEmptyColor,
            radius: radius,
            child: Icon(
              Icons.file_upload,
              size: 50,
              color: Colors.black,
            ));
      } else {
        return CircleAvatar(
          backgroundColor: circleEmptyColor,
          radius: radius,
          backgroundImage: FileImage(registerProfileProvider.imageFile!),
        );
      }
    });
  }
}
/*class CirclePhoto extends StatelessWidget {
  final String? url;

  const CirclePhoto({Key? key, this.url}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (url == null) {
      return ClipRRect(
          borderRadius: BorderRadius.circular(100),
          child: Container(
            color: Colors.black38,
            height: MediaQuery.of(context).size.width * 0.4,
            width: MediaQuery.of(context).size.width * 0.4,
            child: Center(
              child: Icon(
                Icons.upload_file,
                size: 50,
                color: Colors.white,
              ),
            ),
          ));
    } else {
      return Image(image: NetworkImage(url!));
    }
  }
}
*/
