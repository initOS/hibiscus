/**********************************************************************
 * $Source: /cvsroot/hibiscus/hibiscus/updates/update0013.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/01/04 17:43:30 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import de.willuhn.jameica.hbci.rmi.HBCIDBService;
import de.willuhn.jameica.hbci.server.DBSupportH2Impl;
import de.willuhn.jameica.hbci.server.DBSupportMcKoiImpl;
import de.willuhn.jameica.hbci.server.DBSupportMySqlImpl;
import de.willuhn.jameica.hbci.server.HBCIUpdateProvider;
import de.willuhn.logging.Logger;
import de.willuhn.sql.ScriptExecutor;
import de.willuhn.sql.version.Update;
import de.willuhn.sql.version.UpdateProvider;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;


/**
 * Neue Spalte "kommentar" in Konten.
 */
public class update0013 implements Update
{
  private Map statements = new HashMap();
  
  /**
   * ct
   */
  public update0013()
  {
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
      "alter table konto add kommentar varchar(1000) NULL;\n");

    // Update fuer McKoi
    statements.put(DBSupportMcKoiImpl.class.getName(),
      "alter table konto add kommentar varchar(1000) NULL;\n");
    
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
      "alter table konto add kommentar text NULL;\n");
  }

  /**
   * @see de.willuhn.sql.version.Update#execute(de.willuhn.sql.version.UpdateProvider)
   */
  public void execute(UpdateProvider provider) throws ApplicationException
  {
    HBCIUpdateProvider myProvider = (HBCIUpdateProvider) provider;
    I18N i18n = myProvider.getResources().getI18N();

    // Wenn wir eine Tabelle erstellen wollen, muessen wir wissen, welche
    // SQL-Dialekt wir sprechen
    String driver = HBCIDBService.SETTINGS.getString("database.driver",null);
    String sql = (String) statements.get(driver);
    if (sql == null)
      throw new ApplicationException(i18n.tr("Datenbank {0} wird nicht unterst�tzt",driver));
    
    try
    {
      Logger.info("create sql table for update0013");
      ScriptExecutor.execute(new StringReader(sql),myProvider.getConnection(),myProvider.getProgressMonitor());
      myProvider.getProgressMonitor().log(i18n.tr("Tabelle 'Konto' um Spalte 'kommentar' erweitert"));
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      Logger.error("unable to execute update",e);
      throw new ApplicationException(i18n.tr("Fehler beim Ausf�hren des Updates"),e);
    }
  }

  /**
   * @see de.willuhn.sql.version.Update#getName()
   */
  public String getName()
  {
    return "Erweitert die Tabelle \"konto\" um eine Spalte \"kommentar\"";
  }

}


/*********************************************************************
 * $Log: update0013.java,v $
 * Revision 1.1  2009/01/04 17:43:30  willuhn
 * @N BUGZILLA 532
 *
 **********************************************************************/