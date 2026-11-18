package com.sshtunnelpro.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.net.Uri;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private LinearLayout page, fields;
    private Spinner mode;
    private EditText host, port, user, pass, proxyHost, proxyPort, sni, payload, dns, udpgw, config;
    private TextView status;
    private String activeTab = "SSH";

    private final String[] sshModes = {
        "SSH-Direct", "SSH-Proxy", "SSH-Payload", "SSH-Proxy-Payload",
        "SSH-TLS", "SSH-TLS-Proxy", "SSH-TLS-Payload",
        "SSH-TLS-Proxy-Payload", "SSH-DNSTT"
    };
    private final String[] xrayModes = {"VLESS", "VMess", "Trojan", "Xray Custom JSON"};

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        build();
    }

    private void build() {
        ScrollView scroll = new ScrollView(this);
        page = new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(24, 22, 24, 28);
        scroll.addView(page);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("SSH TUNNEL PRO"); title.setTextSize(25);
        title.setTypeface(null, Typeface.BOLD); title.setPadding(0, 8, 0, 18);
        page.addView(title);

        LinearLayout tabs = new LinearLayout(this);
        addTab(tabs, "SSH"); addTab(tabs, "Xray"); addTab(tabs, "Config"); addTab(tabs, "Logs");
        page.addView(tabs);

        status = new TextView(this); status.setText("Status: DISCONNECTED");
        status.setTextSize(16); status.setPadding(0, 18, 0, 14); page.addView(status);

        fields = new LinearLayout(this); fields.setOrientation(LinearLayout.VERTICAL); page.addView(fields);
        renderFields();
    }

    private void addTab(LinearLayout row, String name) {
        Button b = new Button(this); b.setText(name);
        b.setOnClickListener(v -> { activeTab = name; renderFields(); });
        row.addView(b, new LinearLayout.LayoutParams(0, -2, 1));
    }

    private EditText input(String label, String hint) {
        TextView t = new TextView(this); t.setText(label); t.setTextSize(15);
        t.setPadding(0, 12, 0, 3); fields.addView(t);
        EditText e = new EditText(this); e.setSingleLine(false); e.setTextSize(16);
        e.setHint(hint); fields.addView(e); return e;
    }

    private void renderFields() {
        fields.removeAllViews();
        if (activeTab.equals("SSH") || activeTab.equals("Xray")) {
            TextView heading = new TextView(this);
            heading.setText(activeTab.equals("SSH") ? "SSH CONNECTION" : "XRAY ENGINE");
            heading.setTextSize(19); heading.setTypeface(null, Typeface.BOLD); fields.addView(heading);

            mode = new Spinner(this);
            ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                    activeTab.equals("SSH") ? sshModes : xrayModes);
            mode.setAdapter(a); fields.addView(mode);

            host = input("Server Host", "example.com");
            port = input("Server Port", activeTab.equals("SSH") ? "22" : "443");
            if (activeTab.equals("SSH")) {
                user = input("SSH Username", "username");
                pass = input("SSH Password", "password");
                proxyHost = input("Proxy Host (optional)", "proxy.example.com");
                proxyPort = input("Proxy Port (optional)", "8080");
                sni = input("TLS SNI (optional)", "www.example.com");
                payload = input("Custom Payload (optional)", "CONNECT [host_port] HTTP/1.1");
                udpgw = input("UDPGW Port", "7300");
                CheckBox udp = new CheckBox(this); udp.setText("Enable UDPGW"); udp.setChecked(true); fields.addView(udp);
            } else {
                sni = input("SNI / Server Name", "www.example.com");
                payload = input("Transport Path / Service Name", "/ws");
                config = input("Share Link / Xray JSON", "Paste vless://, vmess://, trojan:// or JSON");
            }
            Button connect = new Button(this); connect.setText("CONNECT");
            connect.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Engine belum dipasang")
                .setMessage("Antaramuka dan medan konfigurasi sudah tersedia. Enjin SSH/Xray sebenar belum disertakan dalam prototaip ini; butang Connect belum membuat terowong.")
                .setPositiveButton("OK", null).show());
            fields.addView(connect);
            TextView note = new TextView(this);
            note.setText("Nota: protokol yang dipilih hanya menetapkan jenis konfigurasi. Sambungan sebenar memerlukan integrasi enjin SSH/Xray dan ujian VPNService.");
            note.setTextSize(12); fields.addView(note);
        } else if (activeTab.equals("Config")) {
            TextView h = new TextView(this); h.setText("CONFIG MANAGER"); h.setTextSize(19);
            h.setTypeface(null, Typeface.BOLD); fields.addView(h);
            config = input("Config / Share Link / JSON", "Paste config di sini");
            Button imp = new Button(this); imp.setText("IMPORT FILE");
            imp.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.setType("*/*"); i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(i, 10);
            }); fields.addView(imp);
            Button exp = new Button(this); exp.setText("EXPORT CONFIG");
            exp.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                i.setType("text/plain"); i.putExtra(Intent.EXTRA_TITLE, "tunnel-config.txt");
                startActivityForResult(i, 11);
            }); fields.addView(exp);
            Button detect = new Button(this); detect.setText("DETECT PROTOCOL");
            detect.setOnClickListener(v -> detectProtocol()); fields.addView(detect);
        } else {
            TextView h = new TextView(this); h.setText("CONNECTION LOGS"); h.setTextSize(19);
            h.setTypeface(null, Typeface.BOLD); fields.addView(h);
            TextView log = new TextView(this);
            log.setText("Belum ada sesi sambungan.\nLog akan dipaparkan selepas enjin SSH/Xray diintegrasikan.");
            fields.addView(log);
        }
    }

    private void detectProtocol() {
        String s = config.getText().toString().trim().toLowerCase();
        String result = s.startsWith("vless://") ? "VLESS" :
            s.startsWith("vmess://") ? "VMess" :
            s.startsWith("trojan://") ? "Trojan" :
            (s.startsWith("{") ? "Xray JSON" : "Format tidak dikenali");
        new AlertDialog.Builder(this).setMessage("Format dikesan: " + result)
            .setPositiveButton("OK", null).show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null || data.getData() == null) return;
        Uri uri = data.getData();
        try {
            if (requestCode == 10) {
                InputStream in = getContentResolver().openInputStream(uri);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[4096]; int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                in.close();
                String value = out.toString("UTF-8");
                config.setText(value);
                Toast.makeText(this, "Config diimport", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 11) {
                OutputStream out = getContentResolver().openOutputStream(uri);
                out.write(config.getText().toString().getBytes(StandardCharsets.UTF_8));
                out.close();
                Toast.makeText(this, "Config dieksport", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ralat fail: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
