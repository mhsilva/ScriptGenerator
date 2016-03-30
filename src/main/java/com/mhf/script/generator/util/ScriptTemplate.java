/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mhf.script.generator.util;

/**
 *
 * @author Matheus
 */
public class ScriptTemplate {

    public static String headerData = "declare\n"
            + "  v_n_id_saas_partition   number;\n"
            + "  v_n_id_user_exit        &&TAXIT_USER..FMK_USER_EXIT.ID_USER_EXIT%type;\n"
            + "  v_n_id_script_editor    &&TAXIT_USER..FMK_SCRIPT_EDITOR.ID_SCRIPT_EDITOR%type;\n"
            + "  v_n_id_script_editor_cst &&TAXIT_USER..FMK_SCRIPT_EDITOR_CST.ID_SCRIPT_EDITOR_CST%type;\n"
            + "  v_s_script_name         &&TAXIT_USER..FMK_SCRIPT_EDITOR.S_SCRIPT_NAME%type := ''{0}'';\n"
            + "  v_s_user_exit_name      &&TAXIT_USER..FMK_USER_EXIT.S_NAME_USER_EXIT%type := ''{1}'';\n"
            + "  v_cl_source             clob := EMPTY_CLOB;\n"
            + "  v_s_functionality_name  &&TAXIT_USER..FMK_FUNCTIONALITY.S_FUNCTIONALITY_NAME%type := ''{2}'';\n"
            + "  v_n_order				  number := ''{3}'';\n"
            + "  v_s_saas_name			  varchar2(250)	  := ''&&PARTITION_NAME'';\n";
    public static String headerProcedures = "  \n"
            + "  procedure PRC_ADD_SOURCE (v_s_source in varchar2) is \n"
            + "  begin\n"
            + "    DBMS_LOB.WRITEAPPEND(v_cl_source, length(v_s_source), v_s_source);\n"
            + "  end PRC_ADD_SOURCE;   \n"
            + "\n"
            + "  function FNC_GET_SAAS_PARTITION(p_s_saas_name in varchar2) return number as\n"
            + "  v_n_return number;\n"
            + "  begin\n"
            + "    --\n"
            + "    begin\n"
            + "	  select id_saas_partition\n"
            + "	    into v_n_return\n"
            + "	    from &&TAXIT_USER..fmk_saas_partition a\n"
            + "	   where a.s_company_abbreviation = p_s_saas_name;\n"
            + "    exception\n"
            + "	  when others then\n"
            + "	    v_n_return := null;\n"
            + "    end;\n"
            + "    --\n"
            + "    return v_n_return;\n"
            + "  end FNC_GET_SAAS_PARTITION;\n"
            + "\n"
            + "begin\n"
            + "  \n"
            + "  DBMS_LOB.CREATETEMPORARY(v_cl_source, true, DBMS_LOB.SESSION);\n"
            + "  v_n_id_saas_partition := FNC_GET_SAAS_PARTITION(v_s_saas_name); --Criar\n";
    public static String staticInsert = "  begin\n"
            + "    insert into &&TAXIT_USER..FMK_SCRIPT_EDITOR\n"
            + "      (ID_SCRIPT_EDITOR,\n"
            + "       S_SCRIPT_NAME,\n"
            + "       CL_SOURCE_CODE,\n"
            + "       S_ACTIVE,\n"
            + "       ID_SAAS_PARTITION_CST)\n"
            + "    values\n"
            + "      (&&TAXIT_USER.. FNC_FMK_JPA_SEQ_NEXTVAL('FmkScriptEditor'),\n"
            + "       v_s_script_name,\n"
            + "       null,\n"
            + "       'Y',\n"
            + "       v_n_id_saas_partition)\n"
            + "    returning ID_SCRIPT_EDITOR into v_n_id_script_editor;\n"
            + "  exception\n"
            + "    when dup_val_on_index then\n"
            + "      select ID_SCRIPT_EDITOR\n"
            + "        into v_n_id_script_editor\n"
            + "        from &&TAXIT_USER..FMK_SCRIPT_EDITOR\n"
            + "       where S_SCRIPT_NAME = v_s_script_name and id_saas_partition_cst = v_n_id_saas_partition;\n"
            + "      --\n"
            + "      update &&TAXIT_USER..FMK_SCRIPT_EDITOR\n"
            + "         set S_SCRIPT_NAME         = v_s_script_name,\n"
            + "             CL_SOURCE_CODE        = null,\n"
            + "             S_ACTIVE              = nvl(S_ACTIVE, 'Y')\n"
            + "       where id_script_editor = v_n_id_script_editor;\n"
            + "    when others then\n"
            + "      null;\n"
            + "  end;\n"
            + "  begin\n"
            + "    insert into &&TAXIT_USER..FMK_SCRIPT_EDITOR_CST\n"
            + "      (ID_SCRIPT_EDITOR_CST,\n"
            + "       ID_SCRIPT_EDITOR,\n"
            + "       CL_SOURCE_CODE,\n"
            + "       S_ACTIVE,\n"
            + "       S_CUSTOM_ON_OFF,\n"
            + "       N_LAST_RELEASE,\n"
            + "       ID_SOURCE_CODE_STATUS,\n"
            + "       ID_SAAS_PARTITION)\n"
            + "    values\n"
            + "      (&&TAXIT_USER.. FNC_FMK_JPA_SEQ_NEXTVAL('FmkScriptEditorCst'),\n"
            + "       v_n_id_script_editor,\n"
            + "       v_cl_source,\n"
            + "       'Y',\n"
            + "       'Y',\n"
            + "       1,\n"
            + "       (select ID_DOMAIN\n"
            + "          from &&TAXIT_USER..FMK_DOMAIN\n"
            + "         where S_DICTIONARY_KEY = 'IT_SCRIPT_EDITOR_STATUS_VALIDATED'), "
            + "       v_n_id_saas_partition) returning ID_SCRIPT_EDITOR_CST into v_n_id_script_editor_cst;\n"
            + "  exception\n"
            + "    when dup_val_on_index then\n"
            + "      update &&TAXIT_USER..FMK_SCRIPT_EDITOR_CST\n"
            + "         set ID_SCRIPT_EDITOR      = v_n_id_script_editor,\n"
            + "             CL_SOURCE_CODE        = v_cl_source,\n"
            + "             S_ACTIVE              = nvl(S_ACTIVE, 'Y'),\n"
            + "             S_CUSTOM_ON_OFF       = nvl(S_CUSTOM_ON_OFF, 'Y'),\n"
            + "             N_LAST_RELEASE        = (SELECT NVL((MAX(sv.N_RELEASE)+1),1) FROM &&TAXIT_USER..FMK_SCRIPT_EDITOR_VERSION sv WHERE sv.ID_SCRIPT_EDITOR_CST = &&TAXIT_USER..FMK_SCRIPT_EDITOR_CST.ID_SCRIPT_EDITOR_CST),\n"
            + "             ID_SOURCE_CODE_STATUS =\n"
            + "             (select ID_DOMAIN\n"
            + "                from &&TAXIT_USER..FMK_DOMAIN\n"
            + "               where S_DICTIONARY_KEY = 'IT_SCRIPT_EDITOR_STATUS_VALIDATED'),\n"
            + "             ID_SAAS_PARTITION     = v_n_id_saas_partition\n"
            + "       where ID_SCRIPT_EDITOR = v_n_id_script_editor and ID_SAAS_PARTITION = v_n_id_saas_partition\n"
            + "	   returning ID_SCRIPT_EDITOR_CST into v_n_id_script_editor_cst;\n"
            + "    when others then\n"
            + "      null;\n"
            + "  end;\n"
            + "  begin\n"
            + "    insert into &&TAXIT_USER..FMK_USER_EXIT\n"
            + "      (ID_USER_EXIT,\n"
            + "       S_NAME_USER_EXIT,\n"
            + "       S_ENABLED,\n"
            + "       ID_SAAS_PARTITION_CST,\n"
            + "       N_ORDER,\n"
            + "       ID_SCRIPT_EDITOR)\n"
            + "    values\n"
            + "      (&&TAXIT_USER.. FNC_FMK_JPA_SEQ_NEXTVAL('FmkUserExit'),\n"
            + "       NVL(v_s_user_exit_name, upper(v_s_script_name) || '_EXIT'),\n"
            + "       'N',\n"
            + "       v_n_id_saas_partition,\n"
            + "       v_n_order,\n"
            + "       v_n_id_script_editor)\n"
            + "    returning ID_USER_EXIT into v_n_id_user_exit;\n"
            + "  exception\n"
            + "    when dup_val_on_index then\n"
            + "      select ID_USER_EXIT\n"
            + "        into v_n_id_user_exit\n"
            + "        from &&TAXIT_USER..FMK_USER_EXIT\n"
            + "       where S_NAME_USER_EXIT = NVL(v_s_user_exit_name,upper(v_s_script_name) || '_EXIT') and id_saas_partition_cst = v_n_id_saas_partition;\n"
            + "      --\n"
            + "      update &&TAXIT_USER..FMK_USER_EXIT\n"
            + "         set S_NAME_USER_EXIT      = NVL(v_s_user_exit_name,upper(v_s_script_name) || '_EXIT'),\n"
            + "             S_ENABLED             = 'N',\n"
            + "             N_ORDER               = v_n_order,\n"
            + "             ID_SCRIPT_EDITOR      = v_n_id_script_editor\n"
            + "       where S_NAME_USER_EXIT = NVL(v_s_user_exit_name,upper(v_s_script_name) || '_EXIT') and ID_SAAS_PARTITION_CST = v_n_id_saas_partition;\n"
            + "    when others then\n"
            + "      null;\n"
            + "  end;\n"
            + "  begin\n"
            + "    insert into &&TAXIT_USER..FMK_USER_EXIT_CST\n"
            + "      (ID_USER_EXIT_CST,\n"
            + "       ID_USER_EXIT,\n"
            + "       S_ENABLED,\n"
            + "       S_CUSTOM_ON_OFF,\n"
            + "       ID_SAAS_PARTITION,\n"
            + "       N_ORDER,\n"
            + "       ID_SCRIPT_EDITOR)\n"
            + "    values\n"
            + "      (&&TAXIT_USER..FNC_FMK_JPA_SEQ_NEXTVAL('FmkUserExitCst'),\n"
            + "       v_n_id_user_exit,\n"
            + "       'Y',\n"
            + "       'Y',\n"
            + "       v_n_id_saas_partition,\n"
            + "       v_n_order,\n"
            + "       v_n_id_script_editor);\n"
            + "  exception\n"
            + "    when dup_val_on_index then\n"
            + "      update &&TAXIT_USER..FMK_USER_EXIT_CST\n"
            + "         set ID_USER_EXIT      = v_n_id_user_exit,\n"
            + "             S_ENABLED         = nvl(S_ENABLED, 'Y'),\n"
            + "             S_CUSTOM_ON_OFF   = nvl(S_CUSTOM_ON_OFF, 'Y'),\n"
            + "             ID_SAAS_PARTITION = v_n_id_saas_partition,\n"
            + "             N_ORDER           = v_n_order,\n"
            + "             ID_SCRIPT_EDITOR  = v_n_id_script_editor\n"
            + "       where ID_USER_EXIT = v_n_id_user_exit and ID_SAAS_PARTITION = v_n_id_saas_partition;\n"
            + "    when others then\n"
            + "      null;\n"
            + "  end;\n";
    public static String footerData = "  begin\n"
            + "    INSERT\n"
            + "    INTO &&TAXIT_USER..FMK_SCRIPT_EDITOR_VERSION\n"
            + "      (\n"
            + "        ID_SCRIPT_EDITOR_VERSION,\n"
            + "        ID_SCRIPT_EDITOR_CST,\n"
            + "        N_RELEASE,\n"
            + "        D_RELEASE_DATE,\n"
            + "        CL_SOURCE_CODE\n"
            + "      )\n"
            + "      VALUES\n"
            + "      (\n"
            + "        &&TAXIT_USER..fnc_fmk_jpa_seq_nextval('FmkScriptEditorVersion'),\n"
            + "        (SELECT ID_SCRIPT_EDITOR_CST FROM &&TAXIT_USER..FMK_SCRIPT_EDITOR_CST WHERE ID_SCRIPT_EDITOR = v_n_id_script_editor AND ID_SAAS_PARTITION = v_n_id_saas_partition),\n"
            + "        (SELECT NVL((MAX(N_RELEASE)+1),1) FROM &&TAXIT_USER..FMK_SCRIPT_EDITOR_VERSION WHERE ID_SCRIPT_EDITOR_CST = v_n_id_script_editor_cst),\n"
            + "        sysdate,\n"
            + "        v_cl_source\n"
            + "      );\n"
            + "  exception\n"
            + "    when others then\n"
            + "      null;\n"
            + "  end;\n"
            + "  DBMS_LOB.FREETEMPORARY(v_cl_source);\n"
            + "end;\n"
            + "/";
    public static String insertEvent = "begin\n" +
"  insert into &&TAXIT_USER..FMK_USER_EXIT_EVENT\n" +
"    (ID_USER_EXIT_EVENT,\n" +
"     ID_OPERATION_FUNCTIONALITY,\n" +
"     S_EVENT_NAME,\n" +
"     S_EVENT_DESCRIPTION,\n" +
"     S_REF_FUNCTIONALITY)\n" +
"  values\n" +
"    (&&TAXIT_USER..FNC_FMK_JPA_SEQ_NEXTVAL(''FmkUserExitEvent''),\n" +
"     (select ID_OPERATION_FUNCTIONALITY\n" +
"        from &&TAXIT_USER..FMK_OPERATION_FUNCTIONALITY\n" +
"       where S_NAME = ''{0}''\n" +
"         and ID_FUNCTIONALITY in\n" +
"             (select ID_FUNCTIONALITY\n" +
"                from &&TAXIT_USER..FMK_FUNCTIONALITY\n" +
"               where S_FUNCTIONALITY_NAME = v_s_functionality_name)),\n" +
"     ''{1}'',\n" +
"     ''{1}'' || '' event on '' || v_s_functionality_name,\n" +
"     v_s_functionality_name);\n" +
"exception\n" +
"  when dup_val_on_index then\n" +
"    null;\n" +
"end;\n";
    public static String insertUserExitXEvent = "begin\n" +
"  -- link user exit e user exit event\n" +
"  insert into &&TAXIT_USER..FMK_USER_EXIT_USER_EXIT_EVENT\n" +
"    (ID_USER_EXIT_USER_EXIT_EVENT, ID_USER_EXIT_EVENT, ID_USER_EXIT)\n" +
"  values\n" +
"    (&&TAXIT_USER..fnc_fmk_jpa_seq_nextval(''FmkUserExitUserExitEvent''),\n" +
"     (select ID_USER_EXIT_EVENT\n" +
"        from &&TAXIT_USER..FMK_USER_EXIT_EVENT\n" +
"       where S_EVENT_NAME = ''{0}''\n" +
"         and ID_OPERATION_FUNCTIONALITY in\n" +
"             (select ID_OPERATION_FUNCTIONALITY\n" +
"                from &&TAXIT_USER..fmk_operation_functionality a\n" +
"               where a.id_functionality in\n" +
"                     (select id_functionality\n" +
"                        from &&TAXIT_USER..fmk_functionality a\n" +
"                       where a.s_functionality_name = v_s_functionality_name)\n" +
"                 and s_name = ''{1}'')),\n" +
"     v_n_id_user_exit);\n" +
"exception\n" +
"  when dup_val_on_index then\n" +
"    NULL;\n" +
"  when others then\n" +
"    null;\n" +
"end;\n";
}
