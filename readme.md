# Softmax Action Selection Tabanlı İstemci Taraflı Yük Dengeleyici Simülasyonu

## 1. Proje Tanımı

Bu proje, dağıtık sistemlerde K adet farklı sunucudan oluşan bir kümeye (cluster) gelen istekleri optimize ederek dağıtan bir istemci taraflı yük dengeleyici (client-side load balancer) simülasyonudur.

Sunucuların yanıt süreleri zamanla değişen (non-stationary) ve gürültülü (noisy) bir yapıya sahiptir. Bu nedenle klasik sabit dağıtım algoritmaları yerine, geçmiş performans verilerini kullanarak olasılıksal karar veren Softmax Action Selection algoritması uygulanmıştır.

Amaç toplam bekleme süresini (latency) minimize etmek ve toplam ödülü (reward) maksimize etmektir.

---

## 2. Problem Tanımı

Gerçek dünya sistemlerinde sunucuların performansı sabit değildir. Ağ yoğunluğu, işlemci yükü ve sistem trafiği zamanla değişebilir. Bu nedenle sabit kurallarla çalışan yük dengeleme algoritmaları optimal sonuç vermez.

Bu projede:

* K adet sunucu bulunmaktadır.
* N adet istemci isteği gönderilmektedir.
* Farklı algoritmalar karşılaştırmalı olarak test edilmektedir.
* Ortalama gecikme ve P95 gecikme metrikleri hesaplanmaktadır.

---

## 3. Kullanılan Algoritmalar

### 3.1 Random

Her istekte sunuculardan biri tamamen rastgele seçilir.

Özellikleri:

* Performans bilgisi kullanmaz
* Öğrenme içermez
* Sabit zamanlıdır

Zaman karmaşıklığı: O(1)

---

### 3.2 Round-Robin

Sunuculara sırayla istek gönderir.

Özellikleri:

* Her sunucuya eşit sayıda istek gönderir
* Anlık performansı dikkate almaz
* Öğrenme içermez

Zaman karmaşıklığı: O(1)

---

### 3.3 Softmax Action Selection

Her sunucu için bir performans tahmini (Q değeri) tutulur.

Seçim yapılırken:

P(i) = exp(Qi / tau) / toplam(exp(Qj / tau))

Qi: Sunucunun tahmini performansı
Tau: Sıcaklık parametresi

Daha iyi performanslı sunucu daha yüksek olasılıkla seçilir ancak diğer sunucular tamamen dışlanmaz.

Bu sayede sistem hem iyi sunucuyu kullanır hem de değişimleri algılamak için diğer sunucuları ara sıra dener.

Zaman karmaşıklığı: O(K)

---

## 4. Q Değerinin Güncellenmesi

Sunucu seçildikten sonra gecikme ölçülür.

Ödül = - gecikme olarak tanımlanır.

Q güncelleme formülü:

Q = (1 - alpha) * eskiQ + alpha * ödül

Alpha öğrenme oranıdır.

Alpha küçükse sistem yavaş öğrenir.
Alpha büyükse hızlı ancak daha dalgalı öğrenir.

---

## 5. Non-Stationary (Zamanla Değişen) Yapı

Sunucuların performansı sabit değildir.

Server sınıfındaki drift() metodu ile her adımda sunucu gecikmesi küçük rastgele miktarda değiştirilir.

Bu sayede algoritmanın değişen ortama uyum yeteneği test edilir.

---

## 6. Gürültü (Noise)

Gerçek sistemlerde anlık ağ dalgalanmaları olur.

Bu nedenle gecikme hesaplanırken Gaussian dağılımlı gürültü eklenmiştir.

Bu simülasyonu daha gerçekçi hale getirir.

---

## 7. Nümerik Stabilite Problemi

Softmax algoritmasında exp fonksiyonu kullanılır.

Q değerleri büyük olduğunda exp(Q) taşma (overflow) hatasına neden olabilir.

Bu problemi çözmek için Max-Normalization yöntemi uygulanmıştır:

1. En büyük Q değeri bulunur.
2. Tüm Q değerlerinden bu maksimum çıkarılır.
3. Daha sonra exp işlemi uygulanır.

Bu sayede sayısal taşma engellenir ve hesaplama kararlı hale gelir.

---

## 8. Çalışma Zamanı Analizi

Random: O(1)
Round-Robin: O(1)
Softmax: O(K)

Softmax algoritmasında:

* Maksimum değeri bulma
* Üstel hesaplama
* Toplam alma

işlemleri K sunucu için yapılır. Bu nedenle karmaşıklık doğrusal olarak artar.

---

## 9. Performans Metrikleri

Simülasyon sonunda aşağıdaki metrikler hesaplanır:

### Ortalama Gecikme

Toplam gecikme / istek sayısı

### P95 Gecikme

En yavaş yüzde 5'lik isteklerin gecikmesi

### Bağıl Performans

Softmax algoritmasının Random ve Round-Robin algoritmalarına göre yüzde iyileşmesi

---

## 10. Simülasyon Parametreleri

Sunucu Sayısı (K): 5
İstek Sayısı (N): 10.000
Sıcaklık (Tau): 0.5
Öğrenme Oranı (Alpha): 0.1

---

## 11. Kullanılan Teknolojiler

Programlama dili: Java
Geliştirme ortamı: IntelliJ IDEA
Dil modeli desteği: GLM 5.0, Gemini (Agentic kodlama yaklaşımı kapsamında)

---

## 12. Kurulum ve Çalıştırma

1. Depoyu klonlayın veya indirin.
2. Terminali proje klasöründe açın.
3. Derleme:

javac Server.java LoadBalancerSimulation.java

4. Çalıştırma:

java LoadBalancerSimulation

---

## 13. Sonuç

Softmax Action Selection algoritması:

* Öğrenme içerir
* Geçmiş performansı dikkate alır
* Olasılıksal seçim yapar
* Değişen ortama uyum sağlar

Bu nedenle dinamik ve belirsiz ortamlarda klasik yöntemlere göre daha verimli ve adaptif bir çözüm sunmaktadır.
