**Dodanie bloku**
----
  Tworzy w repozytorum graf w kontekście `<IRI>`, zawierający przekazane w treści zapytania (POST) dane RDF w formacie [Turtle](https://www.w3.org/TR/turtle/), a następnie dodaje nowy blok, w którym `dataGraphIri`=`IRI`

* **URL**

  /block/create

* **Metoda:**

  `POST`

*  **Parametry URL**

   **Wymagane:**

   `graphIri=[IRI]`

   **Opcjonalne:**

   Brak

* **Parametry**

  * **Nagłówki żądania**
    **`Accept:`** `text/turtle`
  * **Treść żądania:** dane RDF w formacie [Turtle](https://www.w3.org/TR/turtle/).

* **Odpowiedź - sukces:**

  * **Kod:** 200 <br />
    **Typ zawartości:** `text/plain;charset=UTF-8`<br />
    **Zawartość:**
    `{"dataGraphIri": "...", "dataHash": "...", "hash": "...", "index": "12", ..., "timestamp": "..."}`
 
* **Odpowiedź - błąd:**

  * **Kod:** 400 BAD REQUEST <br />
    **Zawartość:** `<informacje o błędzie>`

* **Przykładowe wywołanie:**

  `curl -H "Content-Type: text/turtle" -d @ZIZPYJJVT4JGEJFM4E41.ttl 'http://localhost:8080/mammoth/create?graphIri=http://lei.info/ZIZPYJJVT4JGEJFM4E41'`
* **Opis:**

  W przypadku innych błędów (np. niewłaściwy format danych RDF) w odpowiedzi zwracane są inne kody, np. 500 Internal Server Error