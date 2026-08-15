دَنگی — راهنمای ساخت APK در Android Studio

این پوشه شامل فایل‌های لازم برای اجرای نسخه V7 دَنگی در WebView اندروید است.

روش پیشنهادی:
1) Android Studio را باز کن.
2) New Project را بزن.
3) قالب Empty Views Activity یا Empty Activity را انتخاب کن.
4) Name را Dangi بگذار.
5) Package name را دقیقاً com.dangi.studio بگذار.
6) Language: Kotlin
7) Minimum SDK: API 24 یا بالاتر
8) پروژه را بساز و صبر کن Gradle Sync تمام شود.

سپس:
9) Android Studio را ببند یا پروژه را Close کن.
10) فایل‌های داخل این پک را روی فایل‌های همان پروژه کپی و Replace کن.
11) پروژه را دوباره با Android Studio باز کن.
12) اگر بالای صفحه Sync Now آمد، بزن.
13) اگر SDK 36 نصب نیست، Android Studio پیشنهاد نصب می‌دهد؛ Install را بزن.
14) برای تست، گوشی را با USB وصل کن و Run را بزن.

ساخت APK تستی:
Build > Build App Bundle(s) / APK(s) > Build APK(s)
فایل معمولاً در:
app/build/outputs/apk/debug/app-debug.apk

ساخت APK نهایی و امضاشده:
Build > Generate Signed Bundle / APK
APK > Next
Create new را بزن و یک Key Store بساز.
پسورد و فایل Key Store را جای امن نگه دار؛ برای آپدیت‌های بعدی همین کلید لازم است.
Build Variant را release انتخاب کن و Finish بزن.

نکته:
اطلاعات دَنگی در WebView/Local Storage اپ ذخیره می‌شود.
پاک کردن Data اپ یا حذف کامل اپ ممکن است اطلاعات محلی را پاک کند؛ بنابراین قبل از استفاده جدی، قابلیت Backup نسخه اندروید را در مرحله بعد کامل‌تر می‌کنیم.
