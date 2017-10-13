import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversitiesAdminComponent } from './universities-admin.component';

describe('UniversitiesAdminComponent', () => {
  let component: UniversitiesAdminComponent;
  let fixture: ComponentFixture<UniversitiesAdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UniversitiesAdminComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UniversitiesAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
