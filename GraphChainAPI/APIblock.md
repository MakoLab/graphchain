**Pobierz wszystkie bloki**
----
  Zwraca listę wszystkich bloków.

* **URL**

  /block

* **Metoda:**

  `GET`

*  **Parametry URL**

   **Wymagane:**

   Brak

   **Opcjonalne:**

   Brak

* **Parametry (POST, PUT)**

  Brak

* **Odpowiedź - sukces:**

  * **Kod:** 200 <br />
    **Typ zawartości:** `application/json;charset=UTF-8`<br />
    **Zawartość:**
    `[{"dataGraphIri": "...", "dataHash": "...", "hash": "...", "index": "11", ..., "timestamp": "..."},{"dataGraphIri": "...", "dataHash": "...", "hash": "...", "index": "10" ...}, {... } .... ]`
 
* **Odpowiedź - błąd:**

  * **Kod:** 204 NO CONTENT <br />
    **Zawartość:** Brak

* **Przykładowe wywołanie:**

  `curl http://localhost:8080/mammoth/block`
* **Opis:**

  W trakcie obsługi wywołania następuje weryfikacja łańcucha (sprawdzenie wartości `hash` dla wszystkich bloków) oraz weryfikacja danych (sprawdzenie wartości `dataHash` dla wszystkich grafów `dataGraphIri`)