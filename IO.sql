-- Create table
create table CSUF_INTERNAL_SUPPLIER
(
  internal_supplier_id       VARCHAR2(6),
  obj_id                     VARCHAR2(36) default SYS_GUID(),
  ver_nbr                    NUMBER(8) default 1,
  internal_supplier_nm       VARCHAR2(60),
  internal_supplier_contact  VARCHAR2(40),
  internal_supplier_phone    VARCHAR2(10),
  internal_supplier_actv_ind VARCHAR2(1)
)
tablespace KFS_DATA_SPACE_01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table CSUF_INTERNAL_SUPPLIER
  add constraint CSUF_INTRNL_SUPPL_P1 primary key (INTERNAL_SUPPLIER_ID)
  using index 
  tablespace KFS_INDEX_SPACE_01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table CSUF_INTERNAL_SUPPLIER
  add constraint CSUF_INTRNL_SUPPL_C0 unique (OBJ_ID)
  using index 
  tablespace KFS_INDEX_SPACE_01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate check constraints 
alter table CSUF_INTERNAL_SUPPLIER
  add constraint CSUF_INTRNL_SUPPL_N1
  check ("INTERNAL_SUPPLIER_ID" IS NOT NULL);
alter table CSUF_INTERNAL_SUPPLIER
  add constraint CSUF_INTRNL_SUPPL_N2
  check ("OBJ_ID" IS NOT NULL);
alter table CSUF_INTERNAL_SUPPLIER
  add constraint CSUF_INTRNL_SUPPL_N3
  check ("VER_NBR" IS NOT NULL);

-- Create table
create table CSUF_INT_ORDER_DOC
(
  fdoc_nbr             VARCHAR2(14) not null,
  obj_id               VARCHAR2(36) not null,
  ver_nbr              NUMBER(8) not null,
  internal_supplier_id VARCHAR2(6),
  fdoc_nxt_exp_nbr     NUMBER(7),
  fdoc_nxt_inc_nbr     NUMBER(7),
  fdoc_nxt_itm_nbr     NUMBER(7),
  fdoc_post_yr         NUMBER(4),
  io_orig_amt          NUMBER(19,2)
)
tablespace KFS_DATA_SPACE_01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table CSUF_INT_ORDER_DOC
  add constraint CSUF_INT_ORDER_DOC_P1 primary key (FDOC_NBR)
  using index 
  tablespace KFS_INDEX_SPACE_01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table CSUF_INT_ORDER_DOC
  add constraint CSUF_INT_ORDER_DOC_C0 unique (OBJ_ID)
  using index 
  tablespace KFS_INDEX_SPACE_01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

-- Create table
create table CSUF_INT_ORDER_ITM
(
  fdoc_nbr           VARCHAR2(14) not null,
  obj_id             VARCHAR2(36) not null,
  ver_nbr            NUMBER(8) not null,
  fdoc_itm_nbr       NUMBER(7) not null,
  fdoc_itm_stck_nbr  VARCHAR2(9),
  fdoc_itm_stck_desc VARCHAR2(40),
  fdoc_itm_srvc_dt   DATE,
  fdoc_itm_qty       NUMBER(7,2),
  fdoc_itm_unit_amt  NUMBER(19,2),
  fdoc_unit_msr_cd   VARCHAR2(2)
)
tablespace KFS_DATA_SPACE_01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table CSUF_INT_ORDER_ITM
  add constraint CSUF_INT_ORDER_ITM_P1 primary key (FDOC_NBR, FDOC_ITM_NBR)
  using index 
  tablespace KFS_INDEX_SPACE_01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table CSUF_INT_ORDER_ITM
  add constraint CSUF_INT_ORDER_ITM_C0 unique (OBJ_ID)
  using index 
  tablespace KFS_INDEX_SPACE_01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
