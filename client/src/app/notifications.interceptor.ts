import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { ReqInfoService } from './req-info.service';
import { inject } from '@angular/core';

export const notificationsInterceptor: HttpInterceptorFn = (req, next) => {
  const reqInfoService = inject(ReqInfoService)

  return next(req).pipe(
    catchError((err: any) => {
      if (err.status === 409) {
        reqInfoService.fileAlreadyExists()
      }

      if (err.status === 404) {
        reqInfoService.failLogin()
      }

      return throwError(() => err)
    })
  )
};
