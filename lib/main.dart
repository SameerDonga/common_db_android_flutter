import 'dart:async';
import 'dart:io';

import 'package:background_service/database/database_handler.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'database/objects/Dog.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  DatabaseHandler.db = await DatabaseHandler.getDatabase();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  List<Dog> dogs = [];
  static const stream = const EventChannel('wingquest.stablekernel.io/speech');
  StreamSubscription _event_subscriber;

  void startServiceInPlatform() async {
    if (Platform.isAndroid) {
      MethodChannel methodChannel =
          MethodChannel("com.retroportalstudio.messages");

      String data = await methodChannel.invokeMethod("startService");
      debugPrint(data);

      methodChannel.checkMethodCallHandler((call) async {
        setState(() {});
      });
    }
  }

  @override
  void initState() {
    startServiceInPlatform();
    getDataFromDatabase();
    receiveState();
    super.initState();
  }

  void receiveState() {
    if (_event_subscriber == null) {
      _event_subscriber = stream.receiveBroadcastStream().listen((value) {
        print('is this called');
        getDataFromDatabase();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: ListView.builder(
          itemCount: dogs.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text('${dogs[index].id} - ${dogs[index].name}'),
            );
          },
        ),
      ),
    );
    // return Container(
    //   color: Colors.white,
    //   child: Center(
    //     child: RaisedButton(
    //         child: Text("Start Background"),
    //         onPressed: () async {
    //           await insertData();
    //           startServiceInPlatform();
    //         }),
    //   ),
    // );
  }

  Future<void> insertData() async {
    List<Dog> dogs = await DatabaseHandler().dogs();
    print('insert dog data==> ${dogs.length}');
    int id = dogs.length + 1;
    var fido = Dog(
      id: id,
      name: 'Fido $id',
      age: 35,
    );

    // Insert a dog into the database.
    await DatabaseHandler().insertDog(fido);
  }

  void getDataFromDatabase() async {
    dogs = await DatabaseHandler().dogs();
    setState(() {});
  }
}
