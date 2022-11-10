import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./view/login/login.component";
import {FullLayoutComponent} from "./view/full-layout/full-layout.component";

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'index', component: FullLayoutComponent, pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
