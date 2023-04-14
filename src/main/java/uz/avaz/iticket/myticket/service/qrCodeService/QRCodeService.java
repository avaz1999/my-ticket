package uz.avaz.iticket.myticket.service.qrCodeService;

public interface QRCodeService {
    byte[] generateQRCode(String qrContent, int width, int height);
}
