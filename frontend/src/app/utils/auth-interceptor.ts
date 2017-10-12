import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { forwardRef, Inject, Injectable, Injector } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private authService: AuthService;

  constructor(private injector: Injector) {
    // There's a cyclic dependency between AuthService and the interceptor. We manually retrieve the
    // service using the injector instead. https://github.com/angular/angular/issues/18222
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // https://angular.io/guide/http#setting-new-headers
    this.authService = this.injector.get(AuthService);
    let authRequest = req.clone();
    if (this.authService.getToken() != null) {
      const authHeader = this.authService.getToken();
      authRequest = req.clone({setHeaders: {Authorization: authHeader}});
    }
    return next.handle(authRequest)
  }
}
