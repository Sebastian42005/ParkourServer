import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './view/login/login.component';
import {FormsModule} from "@angular/forms";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import {HttpClientModule} from "@angular/common/http";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {FullLayoutComponent} from "./view/full-layout/full-layout.component";
import {MatTabsModule} from "@angular/material/tabs";
import { UsermanagementComponent } from './components/usermanagement/usermanagement.component';
import { CompanymanagementComponent } from './components/companymanagement/companymanagement.component';
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FullLayoutComponent,
    UsermanagementComponent,
    CompanymanagementComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    MatIconModule,
    HttpClientModule,
    MatSnackBarModule,
    MatTabsModule,
    MatSortModule,
    MatTableModule,
  ],
  providers: [

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
