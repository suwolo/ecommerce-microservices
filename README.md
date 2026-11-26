## Alasan untuk threshold, delay dan fallback
- Asumsikan Order Service dan Inventory Service ini digunakan pada sebuah e-commerce dengan pengunjung sekitar 10.000 setiap harinya
- Timeout : 1s
- Retry : maksimal retry sebanyak 2x dengan delay yang cukup lama sekitar 500ms 
- Circuit Breaker : volume threshold 20, failure ratio 0.3, delay 10s, success threshold sebanyak 2
- Kombinasi tersebut akan membuat system detection bisa lebih cepat menangkap jika ada problem, retry 2x dengan delay 0.5s menjadi awal untuk mendeteksi problem, dan dengan circuit breaker perbandingan 20 : 0.3 berarti ada 6 request yang gagal akan menjadi trigger ada masalah dalam inventory service


## Thread Pool & Event Loop Isolation:

Bagaimana Anda memastikan bahwa kegagalan atau kelambatan pada `InventoryService` tidak memicu thread starvation atau mengganggu reactive event loop pada Quarkus/Vert.x?

- Order Service menggunakan metode synchronous, dan Quarkus REST secara default menggunakan worker threads, tidak menggunakan event-loop threads, sehingga tidak mengganggu jalannya proses asynchronous. Akan tetapi pada proses hit ke Inventory Service, kemungkinan akan terganggu proses background yang berjalan di Quarkus Service, sehingga adanya Timeout selama 1s akan menimimalisir hal tersebut agar tidak membuang resource server.

## Observability & Monitoring:

Metrik atau indikator apa saja yang wajib diekspos ke sistem pemantauan (misal: Prometheus/Datadog) untuk memantau status Circuit Breaker dan tingkat kegagalan integrasi ini secara real-time?

- Status Circuit Breaker
- Request yang gagal
- Latency
- Fallback/Retry activity