import { Routes } from '@angular/router';
import { SignUpFormComponent } from './sign-up-form/sign-up-form.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { RecordsViewComponent } from './records-view/records-view.component';
import { HomeComponent } from './home/home.component';
import { SpecsViewComponent } from './specs-view/specs-view.component';
import { ParsedViewComponent } from './parsed-view/parsed-view.component';
import { FlatViewComponent } from './flat-view/flat-view.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { HistoryViewComponent } from './history-view/history-view.component';

export const routes: Routes = [
    {path: "", redirectTo: "login", pathMatch: "full"},
    {path: "signup", component: SignUpFormComponent},
    {path: "login", component: LoginFormComponent},
    {path: "home", component: HomeComponent},
    {path: "records", component: RecordsViewComponent},
    {path: "specs", component: SpecsViewComponent},
    {path: "parsed", component: ParsedViewComponent},
    {path: "flat", component: FlatViewComponent},
    {path: "history", component: HistoryViewComponent},
    {path: "admin", component: UserManagementComponent}
];
