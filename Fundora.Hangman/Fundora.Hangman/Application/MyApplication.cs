using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using SharedCode;
using SharedCode.Models;
using SQLite;

namespace Fundora.Hangman.Application
{
    [Application(Debuggable = true)]
    public class MyApplication : Android.App.Application
    {
        private static String RomanianDbName = "Fundora.Hangman.Romanian";
        private static String DbPath;
        public static SQLiteAsyncConnection sqLConnection { get; private set; }
        public static List<Word> Words { get; set; }

        public static List<Difficulty> DifficultyLevels { get; set; }

        public MyApplication(IntPtr javaReference, JniHandleOwnership transfer)
            : base(javaReference, transfer)
        {
        }

        public override void OnCreate()
        {
            try
            {
                base.OnCreate();
                InitializeDB();
                // do application specific things here
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private async Task InitializeDB()
        {
            try
            {
                DbPath = Path.Combine(Android.OS.Environment.ExternalStorageDirectory.ToString(), RomanianDbName);

                //if (!File.Exists(DbPath))
                //{
                using (BinaryReader br = new BinaryReader(Assets.Open(RomanianDbName)))
                {
                    using (BinaryWriter bw = new BinaryWriter(new FileStream(DbPath, FileMode.Create)))
                    {
                        byte[] buffer = new byte[2048];
                        int len = 0;
                        while ((len = br.Read(buffer, 0, buffer.Length)) > 0)
                        {
                            bw.Write(buffer, 0, len);
                        }
                    }
                }
                //}
                sqLConnection = new SQLiteAsyncConnection(DbPath, true);
                await ReadDB();
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private async Task ReadDB()
        {
            try
            {
                Words = await sqLConnection.Table<Word>().ToListAsync();
                DifficultyLevels = await sqLConnection.Table<Difficulty>().ToListAsync();
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }
    }
}