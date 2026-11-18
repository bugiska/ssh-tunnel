# SSH Tunnel Pro — projek asas Android

Ini ialah **prototaip UI/source project**, bukan APK siap untuk tunneling.

## Yang sudah ada
- Menu SSH dan pilihan SSH-Direct, SSH-Proxy, SSH-Payload, SSH-TLS, SSH-DNSTT.
- Menu Xray: VLESS, VMess, Trojan dan Xray Custom JSON.
- Medan host, port, username/password, proxy, SNI, payload dan UDPGW.
- Import/export fail teks dan pengesanan awalan `vless://`, `vmess://`, `trojan://`.
- Tab Logs dan rangka awal Android `VpnService`.

## Yang belum ada (penting)
- Enjin SSH sebenar dan autentikasi SSH.
- Enjin Xray-core sebenar serta konfigurasi inbound/outbound.
- Pengendalian WS/gRPC/TCP/TLS/REALITY.
- TUN packet loop, routing, DNS forwarding, UDPGW dan reconnect.
- QR scanner/generator dan statistik trafik sebenar.

Oleh itu butang CONNECT sengaja memaklumkan bahawa enjin belum dipasang. Jangan anggap prototaip ini sudah boleh menyambung internet melalui SSH/Xray.

## Cara bina APK
1. Buka folder ini menggunakan Android Studio.
2. Gunakan JDK 17.
3. Benarkan Android Studio memuat turun Android Gradle Plugin dan Android SDK 35.
4. Pilih **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. APK debug biasanya berada di `app/build/outputs/apk/debug/app-debug.apk`.

Minimum Android: 8.0 (API 26). Sasaran compile SDK: 35.

## Langkah pembangunan seterusnya
Untuk keluaran yang benar-benar berfungsi, integrasikan versi Xray-core Android yang serasi dan enjin SSH yang sah, kemudian lengkapkan VPNService/TUN routing serta ujian pada peranti sebenar. Jangan masukkan akaun/kata laluan sebenar ke dalam fail config yang dikongsi.
