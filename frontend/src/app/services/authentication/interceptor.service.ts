import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from './token.service';

@Injectable()
export class InterceptorService implements HttpInterceptor {

  constructor(private tokenService: TokenService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
    let newReq = req
    const token = this.tokenService.getToken()
    if(token != null){
      newReq = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token) })
    }
    return next.handle(newReq)
  }

  
}