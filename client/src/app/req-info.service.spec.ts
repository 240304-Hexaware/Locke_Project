import { TestBed } from '@angular/core/testing';

import { ReqInfoService } from './req-info.service';

describe('ReqInfoService', () => {
  let service: ReqInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReqInfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
