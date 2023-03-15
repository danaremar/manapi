import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";
import { NewUser } from "src/app/models/user/new-user";
import { LoginUser } from "src/app/models/user/login-user";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    // URL
    authUrl = environment.backendEndpoint

    // CONSTRUCTOR
    constructor(private httpClient: HttpClient) { }

    /*
    *
    * OPERATIONS
    * 
    */
    public register(newUser: NewUser): Observable<any> {
        return this.httpClient.post<any>(this.authUrl + '/register', newUser);
    }

    public login(loginUser: LoginUser): Observable<any> {
        return this.httpClient.post<any>(this.authUrl + '/login', loginUser);
    }
}