Softmax Action Selection Tabanlı İstemci Taraflı Yük Dengeleyici Simülasyonu
1. Proje Tanımı

Bu proje, dağıtık sistemlerde K adet farklı sunucudan oluşan bir kümeye (cluster) gelen istekleri optimize ederek dağıtan bir istemci taraflı yük dengeleyici (client-side load balancer) simülasyonudur.

Sunucuların yanıt süreleri zamanla değişen (non-stationary distribution) ve gürültülü (noisy) bir yapıya sahiptir. Bu nedenle klasik sabit dağıtım algoritmaları yerine, geçmiş performans verilerini kullanarak olasılıksal karar veren Softmax Action Selection algoritması uygulanmıştır.

Projenin temel amacı, toplam bekleme süresini (latency) minimize etmek ve toplam ödülü (reward) maksimize etmektir.

2. Problem Tanımı

Gerçek dünya sistemlerinde:

Sunucuların yanıt süreleri sabit değildir.

Ağ yoğunluğu değişebilir.

İşlemci yükü dalgalanabilir.

Anlık gecikmeler oluşabilir.

Bu nedenle, her zaman aynı sırayla veya rastgele seçim yapmak optimal değildir.

Bu projede:

K adet sunucu bulunmaktadır.

N adet istemci isteği gönderilmektedir.

Her algoritma için toplam ve ortalama gecikme hesaplanmaktadır.

Softmax algoritmasının klasik yöntemlere göre performansı analiz edilmektedir.

3. Kullanılan Algoritmalar

Simülasyon kapsamında üç farklı yük dengeleme yaklaşımı karşılaştırılmıştır.

3.1 Random (Rastgele Seçim)

Her istekte sunuculardan biri tamamen rastgele seçilir.

Özellikleri:

Performans bilgisini kullanmaz.

Basit ve sabit zamanlıdır.

Öğrenme içermez.

Zaman karmaşıklığı: O(1)

3.2 Round-Robin

Sunuculara sırayla istek gönderir.

Özellikleri:

Her sunucuya eşit sayıda istek gönderir.

Anlık performansı dikkate almaz.

Öğrenme içermez.

Zaman karmaşıklığı: O(1)

3.3 Softmax Action Selection

Bu algoritma her sunucu için bir değer tahmini (Q değeri) tutar.

Her seçimde:

Q değerleri kullanılarak olasılıklar hesaplanır.

Daha iyi performans gösteren sunucu daha yüksek olasılıkla seçilir.

Ancak diğer sunuculara da küçük bir seçilme şansı tanınır.

Seçim sonrası gecikmeye göre Q değeri güncellenir.

Bu yapı sayesinde sistem:

Geçmişi hatırlar

Performansa göre uyum sağlar

Değişen koşullara adapte olur

Zaman karmaşıklığı: O(K)

K: Sunucu sayısı

4. Teknik Detaylar
4.1 Non-Stationary (Zamanla Değişen Dağılım)

Sunucu performansları sabit değildir.

Server sınıfındaki drift() metodu ile her adımda:

Sunucu gecikmesi küçük rastgele değişimlere uğrar.

Bu sayede sistem dinamik bir ortamda test edilir.

4.2 Gürültü (Noise)

Sunucu yanıt süreleri hesaplanırken:

Gaussian (normal dağılımlı) gürültü eklenmiştir.

Bu, gerçek ağ dalgalanmalarını simüle eder.

Bu sayede model daha gerçekçi hale getirilmiştir.

4.3 Nümerik Stabilite Problemi

Softmax hesaplanırken üstel fonksiyon (exp) kullanılır:

P(i) = exp(Qi / tau) / toplam(exp(Qj / tau))

Q değerleri büyüdüğünde exp() taşma (overflow) problemi oluşturabilir.

Bu problemi çözmek için Max-Normalization uygulanmıştır:

Önce en büyük Q değeri bulunur.

Tüm Q değerlerinden bu maksimum değer çıkarılır.

Daha sonra exp işlemi yapılır.

Bu yöntem sayısal taşmaları engeller ve algoritmayı kararlı hale getirir.

5. Çalışma Zamanı Analizi
Algoritma	Seçim Karmaşıklığı
Random	O(1)
Round-Robin	O(1)
Softmax	O(K)

Softmax algoritması:

Maksimum bulma

Üstel hesaplama

Normalizasyon

işlemleri yaptığı için sunucu sayısına bağlı olarak doğrusal karmaşıklığa sahiptir.

6. Simülasyon Parametreleri

Sunucu Sayısı (K): 5

İstek Sayısı (N): 10.000

Sıcaklık Parametresi (Tau): 0.5

Öğrenme Oranı (Alpha): 0.1

Parametre Açıklamaları

Tau:

Küçük olduğunda en iyi sunucu daha sık seçilir.

Büyük olduğunda seçimler daha rastgele olur.

Alpha:

Yeni verinin eski tahmini ne kadar etkileyeceğini belirler.

Küçük değerler daha yavaş öğrenme sağlar.

Büyük değerler daha hızlı ama daha dalgalı öğrenme sağlar.

7. Ölçülen Performans Metrikleri

Simülasyon sonunda aşağıdaki metrikler hesaplanır:

7.1 Ortalama Gecikme

Toplam gecikme / istek sayısı

Genel sistem performansını gösterir.

7.2 P95 Gecikme

En yavaş %5’lik isteklerin gecikme değeridir.

Sistem kararlılığını ve kötü durum performansını ölçer.

7.3 Bağıl Performans

Softmax algoritmasının:

Random

Round-Robin

algoritmalarına göre yüzde kaç daha iyi performans gösterdiği hesaplanır.

8. Agentic Kodlama Yaklaşımı

Bu projede Softmax algoritması:

Geçmiş performansları saklar.

Her seçim sonrası kendini günceller.

Yeni kararlarını önceki sonuçlara göre verir.

Bu yapı sabit kurallara dayalı değildir.

Bu nedenle sistem:

Ortamı gözlemler

Karar verir

Sonucu değerlendirir

Kendini günceller

Bu döngü nedeniyle agentic (ajan benzeri) bir karar mekanizması içerir.

9. Kullanılan Teknolojiler

Programlama Dili: Java

Geliştirme Ortamı: IntelliJ IDEA

Dil Modeli Desteği: GLM 5.0 / Opus / Gemini / Qwen (Agentic Kodlama yöntemi kapsamında)

10. Kurulum ve Çalıştırma

Depoyu klonlayın veya indirin.

Terminali proje klasöründe açın.

Derleme işlemi:

javac Server.java LoadBalancerSimulation.java

Çalıştırma:

java LoadBalancerSimulation

11. Projenin Teknik Önemi

Bu proje:

Değişen ortamlara uyum sağlayabilen

Öğrenme içeren

Olasılıksal karar veren

Nümerik stabilitesi sağlanmış

bir yük dengeleme yaklaşımını göstermektedir.

Klasik yöntemlere göre:

Daha düşük ortalama gecikme

Daha iyi P95 değeri

Daha dengeli sistem davranışı

sağlamaktadır.