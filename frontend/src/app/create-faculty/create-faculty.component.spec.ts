import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFacultyComponent } from './create-faculty.component';

describe('CreateFacultyComponent', () => {
  let component: CreateFacultyComponent;
  let fixture: ComponentFixture<CreateFacultyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateFacultyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateFacultyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
