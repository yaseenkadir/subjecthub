import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSubjectComponent } from './create-subject.component';

describe('CreateSubjectComponent', () => {
  let component: CreateSubjectComponent;
  let fixture: ComponentFixture<CreateSubjectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateSubjectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateSubjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
