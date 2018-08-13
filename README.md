# HSNR-Coin-3rd-Iteration

in diesem Project gibt es zwei Anfangsknoten für die Software. Die erste Knote ist die Hauptklasse für den "First Server/Client" der als
erste Knote im Netzwerk agieren soll. Die erste Knopte ist nur eine cmd-Software, die keine User Interface hat.
Nachdem die erste Knote online ist, kann man die andere Clients, unsere ausführen. Die Hauptfunktionalitäten findet man hauptsächlich in dem 
zweiten CLient

die .java-Datei von dem "First Server/Client" kann man in dem folgenden Ordner finden:
src/server/MainFirstServer.java

die Datei von "Client" kann man in dem fiolgenden Ordner finden:
src/Client/MainStartScreen.java

Auser den .java Dateien gibt es die Startknoten auch in der ausführbarem .jar Format. Die Daten kann man direkt im root von unserem
Repository finden:

Niedercoin 1st Client.jar - das "First Server"
Niedercoin Client.jar - das "Client"

Wenn Niedercoin Client.jar ausgeführt wird, wird als erstres das Blockchain vorbereitet (es werden 4 Testblöcke gemint) und erst danach
das GUI gestartet. Das könnte 10 bis 15 Sekunden durch das Proof-of-Work vewrursacht werden.

Der eingestellter Schwiwrigkeitsgrad steht zurzeit auf 5. Es dauert im Durchscnitt 3 Sekunden, um ein Block mit dem Schwierigkeitsgrad
von 5 zu minen.
