### Debug Project with IntelliJ

1. In a terminal type the following command:

```
sbt run -jvm-debug <port-number>
```

2. In IntelliJ go to the `Run` menu and select `Edit Configuration` menu
3. In the left panel on the pop-up window click on `+` sign
4. Select `Remote JVM Debug`
5. Give it a name
6. Enter the port number and press `Apply` and then `OK`
7. Go back into the `Run` menu
8. and select `Debug Application Name`
9. you are ready to debug now.