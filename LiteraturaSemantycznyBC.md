#BLONDiE
- Blockchain Ontology with Dynamic Extensibility
- Twórca: Msc. Héctor E. Ugarte R. (Rheinische Friedrich-Wilhelms-Universität Bonn - Germany)
- github: https://github.com/hedugaro/Blondie
- publikacje:
	- https://hedugaro.github.io/Linked-Blockchain-Data/
	- https://semanticblocks.wordpress.com
	- Linked Data Indexing of Distributed Ledgers, Allan Third, John Domingue (The Open University, Milton Keynes, United Kingdom), 2017, 26th International World Wide Web Conference


BLONDiE jest ontologią OWL zaprojektowaną do opisu pojęć i relacji związanych z blockchainem.  W przeciwieństwie do ontologii EthOn, ma być generyczną, tj. niezależną od konkretnej implementacji, reprentacją blockchaina. BLONDiE, podobnie jak EthOn, zawiera pojęcia takie jak "blok", "transakcja", różne typy kont (“balance account”, “contract account”) oraz atrybuty tych typów, takie jak  “transaction payload” and “miner address”. W przeciwieństwie do EthOn, BLONDiE posiada również pojęcia charakterystyczne dla różnych implementacji blockchaina - na przykład "BitcoinBlockHeader" i "EthereumBlockHeader" będących podklasami "BlockHeader". Możliwe jest zatem użycie BLONDiE do opisu transakcji wystęĻujących w różnych blockchainach. Ze wszystkich ontologii dla blockchaina  BLONDiE stanow najbogatszą reprezentację.

#EthOn
- The Ethereum Ontology
- Twórcy: Johannes Pfeffer, Alex Beregszazi, Casey Detrio, Herman Junge, Joseph Chow, Marian Oancea, Maurycy Pietrzak, Shahan Khatchadourian, Stefano Bertolo
- github: https://github.com/ConsenSys/EthOn
- publikacje:
	- http://ethon.consensys.net/EthOn_spec.html
	- https://github.com/ethereum/wiki/wiki/White-Paper
	- https://github.com/ethereum/yellowpaper

EthOn jest ontologią OWL stworzoną w celu opisania Ethereum. Opisuje ona takie klasy, jak blok, konto, wiadomość, sieć i stan, a także bardziej konkretne typy, takie jak "kontrakt" i relacje takie jak "blok rodzicielski". EthOn jest na bardzo wczesnym etapie rozwoju.

Wartym odnotowania jest zintegrowanie ontologii EthOn z ontologią FIBO (Financial Industry Business Ontology)

- zob: http://finregont.com/2017/02/21/ethereum_fibo_alignment/

#Flex Ledger 1.0 and the Web Ledger Protocol 1.0
- A flexible format and protocol for decentralized ledgers on the Web
- Twórcy: Manu Sporny and Dave Longley (z firmy Digital Bazaar)
- publikacje:
	- https://w3c.github.io/web-ledger/
	- https://digitalbazaar.github.io/flex-ledger/

Projekt oferuje model danych i składnię do reprezentacji zbioru uporządkowanych zdarzeń w zdecentralizowanym systemie w sposób, który może być kryptograficznie zabezpieczony i zweryfikowany. Głównym celem tego formatu jest elastyczność. Umożliwia on jednocześnie zastosowanie algorytmu konsensusu oraz reprezentację danych historycznych, które można zapisać w księdze (ang. ledger). Format i protokół Web Ledger mogą być wykorzystywane przez istniejące systemy (np. Bitcoin, Ethereum itp.) w celu zapewnienia interoperacyjności ich danych.

Web Ledger posiada również HTTP API umożłiwiające interakcję z różnymi typami blockchaina. Możliwe jest również wykonywanie kwerend SPARQL. 

Struktura bloków jest wyrażona za pomocą serializacji JSON-LD. Pewną wadą jest brak jednoznacznej specyfiakcji oraz semantyki.


##Linked Data Signatures 1.0

Ta specyfikacja opisuje mechanizm zapewniający autentyczność i integralność danych w blockchainie przy użyciu podpisów cyfrowych.

- Twórcy: Manu Sporny i Dave Longley (firma Digital Bazaar)
- Publikacja: https://w3c-dvcg.github.io/ld-signatures/

#BOScoin

- BOScoin: Self-Evolving Cryptocurrency Platform
- github: https://github.com/owlchain

BOScoin jest platformą umożłiwiającą tworzenie kontraktów, których egzekucja jest rozstrzygalna. Dodatkowo umożliwia opis samych kontraktów za pomocą ontologii OWL.
 
