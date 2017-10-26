**Pobierz blok**
----
  Zwraca zawartość bloku o indeksie `blockIndex`.

* **URL**

  /block/:blockIndex

* **Metoda:**

  `GET`

*  **Parametry URL**

   **Wymagane:**

   `blockIndex=[integer]`

   **Opcjonalne:**

   Brak

* **Parametry (POST, PUT)**

  Brak

* **Odpowiedź - sukces:**

  * **Kod:** 200<br />
    **Typ zawartości:** `application/json;charset=UTF-8`<br />
    **Zawartość:**
    `{"dataGraphIri": "...", "dataHash": "...", "hash": "...", "index": "11", ..., "timestamp": "..."}`
 
* **Odpowiedź - błąd:**

  * **Kod:** 404 NOT FOUND<br />
    **Zawartość:** Brak

* **Przykładowe wywołanie:**

  `curl http://localhost:8080/mammoth/block/12`
* **Opis:**

  W odpowiedzi nie ma zawartości grafu `dataGraphIri`