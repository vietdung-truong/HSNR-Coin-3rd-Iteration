# HSNR-Coin-3rd-Iteration

in diesem Project gibt es zwei Anfangsknoten für die Software. Der erste Knoten ist die Hauptklasse für den "First Server/Client", der als
erster Knoten im Netzwerk agieren soll. Der erste Knoten ist nur eine cmd-Software, die kein User Interface hat.
Nachdem dem der erste Knoten online ist, kann man die andere Clients, ausführen. Die Hauptfunktionalitäten findet man hauptsächlich in dem zweiten CLient.

die .java-Datei von dem "First Server/Client" kann man in dem folgenden Ordner finden:

src/server/MainFirstServer.java

die Datei von "Client" kann man in dem fiolgenden Ordner finden:

src/Client/MainStartScreen.java

Auser den .java Dateien gibt es die Startknoten auch in der ausführbarem .jar Format. Die Daten kann man direkt im root von unserem
Repository finden:

Niedercoin 1st Client.jar - der "First Server"

Niedercoin Client.jar - der "Client"

Wenn Niedercoin Client.jar ausgeführt wird, wird als erstes die Blockchain vorbereitet (Es werden 4 Testblöcke gemint) und erst danach
die GUI gestartet. Hier könnten bis zu 10 bis 15 Sekunden durch den Proof-of-Work verursacht werden.

Der eingestellter Schwiwrigkeitsgrad steht zurzeit auf 5. Es dauert im Durchscnitt 3 Sekunden, um ein Block mit dem Schwierigkeitsgrad
von 5 zu minen.
