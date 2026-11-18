# Build APK menggunakan telefon Android sahaja

Anda tidak perlu memasang Android Studio pada telefon. Gunakan GitHub Actions untuk membina APK di pelayan GitHub.

## Langkah
1. Muat turun dan ekstrak ZIP projek ini menggunakan aplikasi pengurus fail.
2. Buka https://github.com dalam Chrome dan daftar/log masuk.
3. Cipta repository baharu, contohnya `ssh-tunnel-pro`.
4. Muat naik semua kandungan folder projek ke repository. Pastikan folder `.github/workflows/` turut dimuat naik. Jika aplikasi GitHub tidak memaparkan folder tersembunyi, gunakan GitHub melalui Chrome dengan pilihan laman Desktop.
5. Buka tab **Actions** dalam repository.
6. Pilih workflow **Build Android APK**. Jika diminta, tekan **I understand my workflows, go ahead and enable them**.
7. Tekan **Run workflow** dan tunggu sehingga proses selesai dengan tanda hijau.
8. Buka hasil run tersebut, pergi ke bahagian **Artifacts**, dan muat turun `SSH-Tunnel-Pro-debug`.
9. Ekstrak fail ZIP artifact. Di dalamnya terdapat `app-debug.apk`. Buka APK itu untuk memasang; benarkan pemasangan daripada sumber tersebut jika Android meminta.

## Perhatian
- Ini menghasilkan APK debug prototaip UI sahaja.
- Butang Connect belum menyambungkan SSH atau Xray. Enjin SSH/Xray dan VPN routing belum diintegrasikan.
- Jangan muat naik konfigurasi yang mengandungi kata laluan sebenar ke repository awam. Gunakan repository Private.
