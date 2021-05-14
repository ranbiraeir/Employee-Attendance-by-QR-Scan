package com.ranbiraeir.attendanceregister;

public interface QRCodeFoundListener {
    void onQRCodeFound(String qrCode);

    void qrCodeNotFound();
}
