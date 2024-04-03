import { HttpInterceptorFn } from '@angular/common/http';
import { UserDataService } from './user-data.service';
import { inject } from '@angular/core';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const userDataService = inject(UserDataService)

  if (userDataService.jwtToken) {
    return next(req.clone({setHeaders: {Authorization: userDataService.jwtToken}}))
  }
  return next(req);
};
