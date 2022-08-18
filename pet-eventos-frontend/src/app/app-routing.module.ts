import { AdminComponent } from './admin/admin.component';
import { GroupComponent } from './group/group.component';
import { FormComponent } from './form/form.component';
import { EventHomeComponent } from './event-home/event-home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginComponent } from './login/login.component';
import { CertificateValidationComponent } from './certificate-validation/certificate-validation.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'event', component: NavbarComponent, children: [
    {path: '', component: EventHomeComponent}
  ]},
  {path: 'group', component: NavbarComponent, children: [
    {path: '', component: GroupComponent}
  ]},

  {path: 'event/:idEvent', component: FormComponent},

  {path: 'admin', component: AdminComponent},
  {path: 'certificate', component: CertificateValidationComponent},
   {path: '**', redirectTo: '/login'}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
