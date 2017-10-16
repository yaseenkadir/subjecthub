import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAssessmentsComponent } from './admin-assessments.component';

describe('AdminAssessmentsComponent', () => {
  let component: AdminAssessmentsComponent;
  let fixture: ComponentFixture<AdminAssessmentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminAssessmentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminAssessmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
