import { NgModule } from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {MatIconModule} from "@angular/material/icon";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {AngularFireAuthModule} from "@angular/fire/compat/auth";
import {AngularFireStorageModule} from "@angular/fire/compat/storage";
import {AngularFirestoreModule} from "@angular/fire/compat/firestore";
import {environment} from "../environments/environment";
import {AngularFireModule} from "@angular/fire/compat";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ToastrModule} from "ngx-toastr";
import {LocationStrategy, NgOptimizedImage, PathLocationStrategy} from "@angular/common";
import {OAuthModule} from "angular-oauth2-oidc";
import {SkipXsrfInterceptor} from "./Config/SkipXsrfInterceptor";
import {AngularFirePerformanceModule} from "@angular/fire/compat/performance";
import {NgxStripeModule} from "ngx-stripe";
import {BrowserModule} from "@angular/platform-browser";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatSidenavModule,
    MatExpansionModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    FormsModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,
    AngularFireStorageModule,
    AngularFireAuthModule,
    AngularFirePerformanceModule,
    HttpClientModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      maxOpened: 3,
      timeOut: 5000,
    }),
    OAuthModule.forRoot(),
    NgOptimizedImage,
    NgxStripeModule.forRoot(environment.stripe_key)
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: SkipXsrfInterceptor, multi: true},
    {provide: LocationStrategy, useClass: PathLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
