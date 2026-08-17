# Hakkak Store — پنل فروش/ادمین

اپلیکیشن اندروید جدا برای فروش اشتراک هککاک: مشتری سفارش می‌ده و عکس رسید آپلود می‌کنه، ادمین تایید/رد می‌کنه، در تایید آدرس پنل/یوزرنیم/پسورد خودکار برای مشتری نمایش داده می‌شه.

## پیش‌نیاز: ساخت پروژه‌ی Firebase (حدود ۵ دقیقه)

1. برو به [console.firebase.google.com](https://console.firebase.google.com) و یک پروژه‌ی جدید بساز.
2. داخل پروژه، یک اپ اندروید اضافه کن با package name: `com.hakkak.store`
3. فایل `google-services.json` که دانلود می‌کنه رو داخل پوشه‌ی `app/` این پروژه (کنار `build.gradle.kts`) بذار.
4. از منوی سمت چپ:
   - **Authentication** → Sign-in method → **Email/Password** رو فعال کن
   - **Firestore Database** → Create database → حالت **production** (بعداً rules رو خودمون می‌ذاریم)
   - **Storage** → Get started (برای عکس رسیدها)

## قوانین امنیتی (Security Rules)

### Firestore rules
تو کنسول Firebase → Firestore → Rules، این رو جایگزین کن:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    function isAdmin() {
      return exists(/databases/$(database)/documents/admins/$(request.auth.uid));
    }
    match /products/{productId} {
      allow read: if request.auth != null;
      allow write: if isAdmin();
    }
    match /orders/{orderId} {
      allow read: if request.auth != null &&
        (resource.data.customerUid == request.auth.uid || isAdmin());
      allow create: if request.auth != null && request.resource.data.customerUid == request.auth.uid;
      allow update: if isAdmin();
    }
    match /admins/{uid} {
      allow read: if request.auth != null;
      allow write: if false; // فقط از کنسول Firebase دستی اضافه/حذف کن
    }
  }
}
```

### Storage rules
تو Storage → Rules:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /receipts/{fileName} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

## چطور یک نفر رو ادمین کنیم؟

بعد از این‌که یک کاربر تو اپ ثبت‌نام کرد (customer عادی می‌شه):
1. تو کنسول Firebase → Authentication، UID اون کاربر رو کپی کن
2. تو Firestore، یک collection به اسم `admins` بساز
3. یک document با ID دقیقاً همون UID بساز (محتواش می‌تونه خالی باشه یا مثلاً `{ "name": "ادمین" }`)

از این به بعد، وقتی اون کاربر وارد اپ می‌شه، مستقیم می‌ره به صفحه‌ی ادمین (لیست سفارش‌ها).

## جریان کار در اپ

**مشتری:**
1. ثبت‌نام/ورود → لیست کالاها
2. زدن "خرید" روی یک کالا → فرم سفارش (نام، شماره، عکس رسید)
3. ثبت سفارش → می‌ره تو "سفارش‌های من" و منتظر تایید می‌مونه
4. وقتی ادمین تایید کرد، همون لحظه (real-time) آدرس پنل/یوزرنیم/پسورد تو "سفارش‌های من" ظاهر می‌شه

**ادمین:**
1. ورود با اکانت ادمین → مستقیم می‌ره لیست سفارش‌های در انتظار
2. زدن "بررسی" روی هر سفارش → عکس رسید رو می‌بینه
3. اگه تایید کرد: آدرس پنل/یوزرنیم/پسورد رو وارد می‌کنه و می‌زنه "تایید" → همون لحظه برای مشتری قابل مشاهده می‌شه
4. اگه رد کرد: می‌زنه "رد"
5. از دکمه‌ی "مدیریت کالاها" می‌تونه کالای جدید (نام، توضیحات، قیمت) اضافه کنه

## نکته درباره‌ی اعلان (Push Notification)
الان مشتری فقط وقتی اپ باز باشه یا صفحه‌ی "سفارش‌های من" رو چک کنه، تغییر وضعیت رو می‌بینه (چون real-time listener هست، نه polling). اگه بخوای وقتی اپ بسته‌ست هم نوتیفیکیشن بره، باید **Firebase Cloud Messaging (FCM)** + یک Cloud Function اضافه کنی که موقع update شدن سفارش، پوش بفرسته. این بخش تو این اسکلت پیاده نشده — بگو اگه بخوای اضافه‌اش کنم.

## باز کردن پروژه
همون مراحل قبلی: Android Studio → Open → پوشه‌ی `hakkak-store` → Gradle Sync (بعد از اضافه کردن `google-services.json`).
